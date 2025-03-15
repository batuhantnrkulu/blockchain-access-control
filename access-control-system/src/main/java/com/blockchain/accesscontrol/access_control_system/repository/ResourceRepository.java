package com.blockchain.accesscontrol.access_control_system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blockchain.accesscontrol.access_control_system.model.Peer;
import com.blockchain.accesscontrol.access_control_system.model.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long>
{
	// Retrieves resources for a given peer
    Page<Resource> findByPeer(Peer peer, Pageable pageable);

    // Retrieves resources that match a resourceName (case insensitive)
    Page<Resource> findByPeerAndResourceNameContainingIgnoreCase(Peer peer, String resourceName, Pageable pageable);
    
    @Query("SELECT r FROM Resource r WHERE ((r.peer.role = 'PRIMARY_GROUP_HEAD' AND r.peer.id <> :peerId) OR (r.peer.group = :group AND r.peer.id <> :peerId))")
    Page<Resource> findForPrimaryGroupHead(@Param("peerId") Long peerId, @Param("group") String group, Pageable pageable);

    @Query("SELECT r FROM Resource r WHERE ((r.peer.role = 'PRIMARY_GROUP_HEAD' AND r.peer.id <> :peerId) OR (r.peer.group = :group AND r.peer.id <> :peerId)) AND LOWER(r.resourceName) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Resource> findForPrimaryGroupHeadWithSearch(@Param("peerId") Long peerId, @Param("group") String group, @Param("search") String search, Pageable pageable);
    
    Page<Resource> findByPeer_Group(String group, Pageable pageable);
    
    @Query("SELECT r FROM Resource r WHERE r.peer.group = :group AND LOWER(r.resourceName) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Resource> findByPeer_GroupWithSearch(@Param("group") String group, @Param("search") String search, Pageable pageable);
}
