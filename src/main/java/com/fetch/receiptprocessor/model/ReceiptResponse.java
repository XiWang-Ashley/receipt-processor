package com.fetch.receiptprocessor.model;

public class ReceiptResponse {
    private String id;

    public ReceiptResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}