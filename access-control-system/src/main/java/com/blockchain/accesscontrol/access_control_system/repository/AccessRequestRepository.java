package com.blockchain.accesscontrol.access_control_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blockchain.accesscontrol.access_control_system.model.AccessRequest;

@Repository
public interface AccessRequestRepository extends JpaRepository<AccessRequest, Long> 
{
	// bring access requests that I requested
	List<AccessRequest> findBySubjectPeer_Username(String username);
    
	// bring access requests that asked me to access my resources
    List<AccessRequest> findByObjectPeer_Username(String username);
}
