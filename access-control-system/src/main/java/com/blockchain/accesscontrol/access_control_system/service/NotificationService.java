package com.blockchain.accesscontrol.access_control_system.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blockchain.accesscontrol.access_control_system.enums.NotificationType;
import com.blockchain.accesscontrol.access_control_system.model.Notification;
import com.blockchain.accesscontrol.access_control_system.model.Peer;
import com.blockchain.accesscontrol.access_control_system.repository.NotificationRepository;

@Service
public class NotificationService 
{
	private final NotificationRepository notificationRepository;
	
	public NotificationService(NotificationRepository notificationRepository)
	{
		this.notificationRepository = notificationRepository;
	}
	
	/**
     * Creates a new notification associated with a peer.
     *
     * If after inserting the new notification there are more than 5 for that peer,
     * the service deletes the oldest notifications.
     */
    public Notification createNotification(Peer peer, String message, NotificationType type) 
    {
        Notification notification = new Notification();
        notification.setPeer(peer);
        notification.setMessage(message);
        notification.setType(type);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        Notification savedNotification = notificationRepository.save(notification);

        // After saving, query the latest notifications for this peer
        List<Notification> notifications = notificationRepository
                .findByPeerUsernameOrderByCreatedAtDesc(peer.getUsername(), 
                    PageRequest.of(0, 10, Sort.by("createdAt").descending()))
                .getContent();

        // Remove older notifications if there are more than 5
        if (notifications.size() > 20) 
        {
            for (int i = 20; i < notifications.size(); i++) 
            {
                notificationRepository.delete(notifications.get(i));
            }
        }
        
        return savedNotification;
    }
    
    public Page<Notification> getNotifications(String username, int page, int size) 
    {
        return notificationRepository.findByPeerUsernameOrderByCreatedAtDesc(username, PageRequest.of(page, size));
    }
    
    public Notification markAsRead(Long notificationId) 
    {
        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        
        if (notification != null) 
        {
        	notification.setIsRead(true);
            return notificationRepository.save(notification);
        }
        
        return null;
    }
}
