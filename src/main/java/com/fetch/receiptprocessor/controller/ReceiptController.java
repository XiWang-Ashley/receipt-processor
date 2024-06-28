package com.fetch.receiptprocessor.controller;

import com.fetch.receiptprocessor.model.*;
import com.fetch.receiptprocessor.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @PostMapping("/receipts/process")
    public ReceiptResponse processReceipt(@RequestBody Receipt receipt) {
        return receiptService.processReceipt(receipt);
    }

    @GetMapping("/receipts/{id}/points")
    public PointsResponse getPoints(@PathVariable String id) {
        return receiptService.getPoints(id);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
