package com.example.adapter.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(schema = "messages")
public class MessageTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionReferenceNumber;
    private String messageType;
    @Enumerated(EnumType.STRING)
    private MessageStatus status;
    private LocalDateTime requestDate;
    private LocalDateTime responseDate;
    private String request;
    private String response;
    private String errorMessage;

}
