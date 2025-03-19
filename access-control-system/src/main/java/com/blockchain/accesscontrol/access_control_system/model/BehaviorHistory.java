package com.blockchain.accesscontrol.access_control_system.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "behavior_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BehaviorHistory 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// i will use many-to-one relationship so that each token history entry is associated with a Peer.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "peer_id", nullable = false)
	private Peer peer;

	@Column(name = "token_amount")
	private Long tokenAmount;
	
	@Column(name = "token_amount_change")
    private Long tokenAmountChange; // Tracks the change in token balance

	@Column(name = "reason")
	private String reason;

	@Column(name = "status_update")
	private LocalDateTime statusUpdate;
}
