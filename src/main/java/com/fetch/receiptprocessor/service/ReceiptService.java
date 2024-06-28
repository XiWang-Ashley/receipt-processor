// src/main/java/com/fetch/receiptprocessor/service/ReceiptService.java
package com.fetch.receiptprocessor.service;

import com.fetch.receiptprocessor.model.Receipt;
import com.fetch.receiptprocessor.model.ReceiptResponse;
import com.fetch.receiptprocessor.model.PointsResponse;
import com.fetch.receiptprocessor.model.Item;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ReceiptService {
    private final Map<String, Receipt> receipts = new HashMap<>();
    private final Map<String, Integer> pointsCache = new HashMap<>();

    public ReceiptResponse processReceipt(Receipt receipt) {
        String id = UUID.randomUUID().toString();
        receipts.put(id, receipt);
        pointsCache.put(id, calculatePoints(receipt));
        return new ReceiptResponse(id);
    }

    public PointsResponse getPoints(String id) {
        Integer points = pointsCache.get(id);
        if (points == null) {
            throw new IllegalArgumentException("Receipt not found");
        }
        return new PointsResponse(points);
    }

    int calculatePoints(Receipt receipt) {
        int points = 0;

        // One point for every alphanumeric character in the retailer name
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(receipt.getRetailer());
        while (matcher.find()) {
            points++;
        }

        // 50 points if the total is a round dollar amount with no cents
        double total = Double.parseDouble(receipt.getTotal());
        if (total == Math.floor(total)) {
            points += 50;
        }

        // 25 points if the total is a multiple of 0.25
        if (total % 0.25 == 0) {
            points += 25;
        }

        // 5 points for every two items on the receipt
        points += (receipt.getItems().size() / 2) * 5;

        // Points for items based on description length
        for (Item item : receipt.getItems()) {
            String description = item.getShortDescription().trim();
            if (description.length() % 3 == 0) {
                double price = Double.parseDouble(item.getPrice());
                points += Math.ceil(price * 0.2);
            }
        }

        // 6 points if the day in the purchase date is odd
        String[] dateParts = receipt.getPurchaseDate().split("-");
        int day = Integer.parseInt(dateParts[2]);
        if (day % 2 != 0) {
            points += 6;
        }

        // 10 points if the time of purchase is after 2:00pm and before 4:00pm
        String[] timeParts = receipt.getPurchaseTime().split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        if (hour == 14 || (hour == 15 && minute == 0)) {
            points += 10;
        }

        return points;
    }
}
