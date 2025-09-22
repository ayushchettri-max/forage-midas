package com.jpmc.midascore.foundation;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.User;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;

@Service
public class TransactionListener {

    private final UserRepository userRepo;
    private final TransactionRecordRepository txRepo;
    private final RestTemplate restTemplate;

    // Store Wilbur's reference
    private User wilbur;

    public TransactionListener(UserRepository userRepo, TransactionRecordRepository txRepo, RestTemplate restTemplate) {
        this.userRepo = userRepo;
        this.txRepo = txRepo;
        this.restTemplate = restTemplate;
        // Load Wilbur once at startup (assuming username is "wilbur")
        this.wilbur = userRepo.findAll()
                              .stream()
                              .filter(u -> u.getUsername().equalsIgnoreCase("wilbur"))
                              .findFirst()
                              .orElse(null);
    }

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core-group")
    public void listen(Transaction transaction) {
        User sender = userRepo.findById(transaction.getSenderId()).orElse(null);
        User recipient = userRepo.findById(transaction.getRecipientId()).orElse(null);

        if (sender != null && recipient != null && sender.getBalance() >= transaction.getAmount()) {
            // Update sender balance
            sender.setBalance(sender.getBalance() - transaction.getAmount());

            // Get incentive from REST API
            Incentive incentive = restTemplate.postForObject(
                    "http://localhost:8080/incentive", 
                    transaction, 
                    Incentive.class
            );

            float incentiveAmount = incentive != null ? incentive.getAmount() : 0f;

            // Update recipient balance including incentive
            recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

            // Save users
            userRepo.save(sender);
            userRepo.save(recipient);

            // Save transaction record with incentive
            TransactionRecord record = new TransactionRecord();
            record.setAmount(transaction.getAmount());
            record.setSender(sender);
            record.setRecipient(recipient);
            txRepo.save(record);
        }
    }

    // Call this at the end to print Wilbur's final balance
    public void printWilburBalance() {
        if (wilbur != null) {
            User refreshedWilbur = userRepo.findById(wilbur.getId()).orElse(null);
            System.out.println("Wilbur's final balance: " + (refreshedWilbur != null ? refreshedWilbur.getBalance() : "not found"));
        }
    }

    // Inner class for Incentive API response
    public static class Incentive {
        private float amount;

        public float getAmount() { return amount; }
        public void setAmount(float amount) { this.amount = amount; }
    }
}
