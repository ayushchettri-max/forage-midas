package com.jpmc.midascore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float amount;

    private float incentive; // <-- add this field

    @ManyToOne
    private User sender;

    @ManyToOne
    private User recipient;

    // Constructors
    public TransactionRecord() {}
    public TransactionRecord(float amount, User sender, User recipient, float incentive) {
        this.amount = amount;
        this.sender = sender;
        this.recipient = recipient;
        this.incentive = incentive; // initialize incentive
    }

    // Getters & Setters
    public Long getId() { return id; }
    public float getAmount() { return amount; }
    public void setAmount(float amount) { this.amount = amount; }

    public float getIncentive() { return incentive; }  // new getter
    public void setIncentive(float incentive) { this.incentive = incentive; }  // new setter

    public User getSender() { return sender; }
    public void setSender(User sender) { this.sender = sender; }
    public User getRecipient() { return recipient; }
    public void setRecipient(User recipient) { this.recipient = recipient; }
}
