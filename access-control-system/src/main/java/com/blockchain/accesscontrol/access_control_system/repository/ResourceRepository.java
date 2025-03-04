package com.blockchain.accesscontrol.access_control_system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.blockchain.accesscontrol.access_control_system.model.Peer;
import com.blockchain.accesscontrol.access_control_system.model.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long>
{
	// Retrieves resources for a given peer
    Page<Resource> findByPeer(Peer peer, Pageable pageable);

    // Retrieves resources that match a resourceName (case insensitive)
    Page<Resource> findByPeerAndResourceNameContainingIgnoreCase(Peer peer, String resourceName, Pageable pageable);
}
