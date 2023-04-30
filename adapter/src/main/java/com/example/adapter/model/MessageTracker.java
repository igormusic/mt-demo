package com.example.adapter.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(schema = "messages")
public class MessageTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 16)
    private String senderReference;
    @Column(length = 30)
    private String messageType;
    @Column(length = 16)
    @Enumerated(EnumType.STRING)
    private MessageStatus status;
    private LocalDateTime requestDate;
    private LocalDateTime responseDate;
    @Column(columnDefinition = "nvarchar(max)")
    private String request;
    @Column(columnDefinition = "nvarchar(max)")
    private String response;
    @Column(columnDefinition = "nvarchar(max)")
    private String errorMessage;


}
