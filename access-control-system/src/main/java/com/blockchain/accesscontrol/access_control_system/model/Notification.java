package com.blockchain.accesscontrol.access_control_system.model;

import java.time.LocalDateTime;

import com.blockchain.accesscontrol.access_control_system.enums.NotificationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relate notification to the peer via peer_id instead of username.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "peer_id", nullable = false)
    private Peer peer;

    @Column(name = "message")
    private String message;
    
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
