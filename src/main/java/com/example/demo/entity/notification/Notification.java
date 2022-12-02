package com.example.demo.entity.notification;

import com.example.demo.entity.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private int id;

    @OneToOne
    @JoinColumn(name = "user_from_id")
    private User userFrom;

    @Column(name = "message")
    private String message;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "date_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dateTime;
}
