package com.blockchain.accesscontrol.access_control_system.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "Validator_List")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidatorList 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "unjoined_peer_id", nullable = false)
    private UnjoinedPeer unjoinedPeer;

    @ManyToOne
    @JoinColumn(name = "validator_id", nullable = false)
    private Peer validator;

    @Column(nullable = false)
    private Boolean approved = false;
    
    @Column(name = "expiry_time", nullable = false)
    private LocalDateTime expiryTime;
}
