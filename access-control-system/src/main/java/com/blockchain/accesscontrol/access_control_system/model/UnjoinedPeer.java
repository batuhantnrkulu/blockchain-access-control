package com.blockchain.accesscontrol.access_control_system.model;

import java.time.LocalDateTime;

import com.blockchain.accesscontrol.access_control_system.utils.EncryptionUtil;

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
	
	@Column(name = "private_key")
    private String privateKey;
	
	@Column(name = "usage_purpose")
	private String usagePurpose;
	
	@Column(name = "is_web_user")
	private Boolean isWebUser = false; 

	@Column(name = "`group`")
	private String group;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();
	
	@Column(name = "validator_count", nullable = false)
    private int validatorCount = 0; // Tracks assigned validators
	
	// encrypt private key when it stores
	public void setPrivateKey(String privateKey, EncryptionUtil encryptionUtil)
	{
		this.privateKey = encryptionUtil.encrypt(privateKey);
	}
	
	// Decrypt private key when retrieving
    public String getPrivateKey(EncryptionUtil encryptionUtil) 
    {
        return encryptionUtil.decrypt(this.privateKey);
    }
}
