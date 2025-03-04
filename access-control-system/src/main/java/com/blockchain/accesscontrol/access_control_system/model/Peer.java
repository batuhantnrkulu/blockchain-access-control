package com.blockchain.accesscontrol.access_control_system.model;

import java.time.LocalDateTime;
import java.util.List;

import com.blockchain.accesscontrol.access_control_system.enums.Role;
import com.blockchain.accesscontrol.access_control_system.enums.Status;
import com.blockchain.accesscontrol.access_control_system.utils.EncryptionUtil;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Peers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Peer 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@Column(name = "bc_address", unique = true)
	private String bcAddress;
	
	// Store the private key (in production, store it encrypted)
    @Column(name = "private_key")
    private String privateKey;
	
	@Column(name = "is_web_user")
	private Boolean isWebUser = false;
	
	@Column(name = "ip_address")
	private String ipAddress;
	
	@Column(name = "usage_purpose")
	private String usagePurpose;
	
	@Column(name = "`group`")
	private String group;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "bc_status", nullable = false)
	private Status bcStatus;
	
	@Column(name = "bc_last_status_update")
	private LocalDateTime bcLastStatusUpdate = LocalDateTime.now();
	
	@Column(name = "erc_20_token_amount")
	private Long erc20TokenAmount = 0L;
	
	@Column(name = "reward_counter")
	private Integer rewardCounter = 0;

	@Column(name = "misbehavior_counter")
	private Integer misbehaviorCounter = 0;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();
	
	@OneToMany(mappedBy = "peer", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Resource> resources;
	
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
