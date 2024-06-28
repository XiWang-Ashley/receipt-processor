package com.fetch.receiptprocessor.service;

import com.fetch.receiptprocessor.model.Item;
import com.fetch.receiptprocessor.model.Receipt;
import com.fetch.receiptprocessor.model.ReceiptResponse;
import com.fetch.receiptprocessor.model.PointsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptServiceTest {

    @InjectMocks
    private ReceiptService receiptService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessReceipt() {
        // Prepare test data
        Receipt receipt = new Receipt();
        receipt.setRetailer("Walmart");
        receipt.setTotal("20.00");
        receipt.setPurchaseDate("2024-06-26");
        receipt.setPurchaseTime("14:30");
        Item item1 = new Item();
        item1.setShortDescription("item1");
        item1.setPrice("10.00");
        Item item2 = new Item();
        item2.setShortDescription("item2");
        item2.setPrice("10.00");
        receipt.setItems(Arrays.asList(item1, item2));

        // Call the method to be tested
        ReceiptResponse receiptResponse = receiptService.processReceipt(receipt);

        // Verify the result
        assertNotNull(receiptResponse.getId());
        assertTrue(receiptService.getPoints(receiptResponse.getId()).getPoints() > 0);
    }

    @Test
    void testGetPoints() {
        // Prepare test data
        Receipt receipt = new Receipt();
        receipt.setRetailer("Walmart");
        receipt.setTotal("20.00");
        receipt.setPurchaseDate("2024-06-26");
        receipt.setPurchaseTime("14:30");
        Item item1 = new Item();
        item1.setShortDescription("item1");
        item1.setPrice("10.00");
        Item item2 = new Item();
        item2.setShortDescription("item2");
        item2.setPrice("10.00");
        receipt.setItems(Arrays.asList(item1, item2));

        // Call the method to be tested
        ReceiptResponse receiptResponse = receiptService.processReceipt(receipt);
        PointsResponse pointsResponse = receiptService.getPoints(receiptResponse.getId());

        // Verify the result
        assertNotNull(pointsResponse);
        assertTrue(pointsResponse.getPoints() > 0);
    }

    @Test
    void testCalculatePoints() {
        // Prepare test data
        Receipt receipt = new Receipt();
        receipt.setRetailer("Walmart");
        receipt.setTotal("20.00");
        receipt.setPurchaseDate("2024-06-26");
        receipt.setPurchaseTime("14:30");
        Item item1 = new Item();
        item1.setShortDescription("item1");
        item1.setPrice("10.00");
        Item item2 = new Item();
        item2.setShortDescription("item2");
        item2.setPrice("10.00");
        receipt.setItems(Arrays.asList(item1, item2));

        // Call the private method through reflection
        int points = receiptService.calculatePoints(receipt);

        // Verify the result
        assertTrue(points > 0);
    }

    @Test
    void testGetPointsInvalidId() {
        // Test with an invalid ID
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            receiptService.getPoints("invalid-id");
        });
        assertEquals("Receipt not found", exception.getMessage());
    }
}

