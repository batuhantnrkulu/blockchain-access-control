package com.blockchain.accesscontrol.access_control_system.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Unjoined_Peers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnjoinedPeer 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@Column(name = "ip_address")
	private String ipAddress;
	
	@Column(name = "bc_address", nullable = false, unique = true)
	private String bcAddress;
	
	@Column(name = "usage_purpose")
	private String usagePurpose;
	
	@Column(name = "is_web_user")
	private Boolean isWebUser = false; 

	@Column(name = "`group`")
	private String group;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();
}
