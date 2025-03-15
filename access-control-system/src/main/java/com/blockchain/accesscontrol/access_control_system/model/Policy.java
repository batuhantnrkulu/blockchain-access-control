package com.blockchain.accesscontrol.access_control_system.model;

import com.blockchain.accesscontrol.access_control_system.enums.Action;
import com.blockchain.accesscontrol.access_control_system.enums.Permission;
import com.blockchain.accesscontrol.access_control_system.enums.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "Policies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Policy 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "object_id", nullable = false)
    private Peer objectPeer;

    @ManyToOne
    @JoinColumn(name = "access_request_id", nullable = false)
    private AccessRequest accessRequest;

    @ManyToOne
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    @Enumerated(EnumType.STRING)
    private Action action;

    @Enumerated(EnumType.STRING)
    private Permission permission;
}
