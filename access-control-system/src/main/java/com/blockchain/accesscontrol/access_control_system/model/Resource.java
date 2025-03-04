package com.blockchain.accesscontrol.access_control_system.model;

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
@Table(name = "Resources")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resource 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "peer_id", nullable = false)
	private Peer peer;
	
	@Column(name = "resource_name", nullable = false, unique = true)
	private String resourceName;
	
	@Column(name = "jpg_resource")
	private String jpgResource;
	
	@Column(name = "data_resource")
	private String dataResource;
}
