package com.example.adapter.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(schema = "messages")
public class TransactionReference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long messageTrackerId;

    @Column(length = 16)
    private String transactionReference;

    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    public TransactionReference(String transactionReference, MessageTracker messageTracker) {
        this.transactionReference = transactionReference;
        this.messageTrackerId = messageTracker.getId();
        this.status= MessageStatus.REQUEST_SENT;
    }

    public TransactionReference() {

    }
}
