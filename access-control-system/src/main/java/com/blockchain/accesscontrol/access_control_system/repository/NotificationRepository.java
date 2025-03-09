package com.blockchain.accesscontrol.access_control_system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.blockchain.accesscontrol.access_control_system.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> 
{
	// Fetch notifications for a given peer username, sorted by creation time (latest first)
	Page<Notification> findByPeerUsernameOrderByCreatedAtDesc(String username, Pageable pageable);
}
