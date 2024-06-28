package com.fetch.receiptprocessor.controller;

import com.fetch.receiptprocessor.model.PointsResponse;
import com.fetch.receiptprocessor.model.Receipt;
import com.fetch.receiptprocessor.model.ReceiptResponse;
import com.fetch.receiptprocessor.service.ReceiptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ReceiptControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReceiptService receiptService;

    @InjectMocks
    private ReceiptController receiptController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(receiptController).build();
    }

    @Test
    public void testProcessReceipt() throws Exception {
        Receipt receipt = new Receipt();
        // Initialize receipt with necessary data...

        ReceiptResponse receiptResponse = new ReceiptResponse("12345");
        when(receiptService.processReceipt(any(Receipt.class))).thenReturn(receiptResponse);

        mockMvc.perform(post("/receipts/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"retailer\": \"Retailer\", \"total\": \"10.00\", \"purchaseDate\": \"2023-06-15\", \"purchaseTime\": \"14:30\", \"items\": [{ \"shortDescription\": \"item1\", \"price\": \"3.00\" }] }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("12345"));
    }

    @Test
    public void testGetPointsValidId() throws Exception {
        String receiptId = "12345";
        PointsResponse pointsResponse = new PointsResponse(50);

        when(receiptService.getPoints(anyString())).thenReturn(pointsResponse);

        mockMvc.perform(get("/receipts/{id}/points", receiptId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.points").value(50));
    }

    @Test
    public void testGetPointsInvalidId() throws Exception {
        String invalidReceiptId = "invalidId";

        when(receiptService.getPoints(anyString())).thenThrow(new IllegalArgumentException("Receipt not found"));

        mockMvc.perform(get("/receipts/{id}/points", invalidReceiptId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Receipt not found"));
    }
}
