package com.charite.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Document(collection = "donations")
public class Donation {
    @Id
    private String id;

    private String userId;
    private String charityActionId;
    private double amount;
    private String paymentMethod;
    private String status;
    private String transactionId;
    private String message;
    private boolean anonymous;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum Status {
        PENDING,
        COMPLETED,
        FAILED,
        REFUNDED
    }

    public enum PaymentMethod {
        CREDIT_CARD,
        BANK_TRANSFER,
        PAYPAL,
        CASH
    }
} 