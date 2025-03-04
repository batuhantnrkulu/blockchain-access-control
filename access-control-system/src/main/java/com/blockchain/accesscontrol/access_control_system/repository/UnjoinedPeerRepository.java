package com.blockchain.accesscontrol.access_control_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blockchain.accesscontrol.access_control_system.model.UnjoinedPeer;

@Repository
public interface UnjoinedPeerRepository extends JpaRepository<UnjoinedPeer, Long> 
{    
	 Optional<UnjoinedPeer> findByBcAddress(String address);
}