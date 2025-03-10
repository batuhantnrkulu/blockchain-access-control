package com.blockchain.accesscontrol.access_control_system.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blockchain.accesscontrol.access_control_system.dto.responses.NotificationResponseDTO;
import com.blockchain.accesscontrol.access_control_system.mapper.NotificationMapper;
import com.blockchain.accesscontrol.access_control_system.model.Notification;
import com.blockchain.accesscontrol.access_control_system.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController 
{
    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    public NotificationController(NotificationService notificationService, NotificationMapper notificationMapper) 
    {
		this.notificationService = notificationService;
		this.notificationMapper = notificationMapper;
	}

	// Fetch notifications for a given peer (using peerId) with pagination.
    @GetMapping
    public ResponseEntity<Page<NotificationResponseDTO>> getNotifications(
            @RequestParam String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Notification> notificationsPage = notificationService.getNotifications(username, page, size);
        Page<NotificationResponseDTO> dtoPage = notificationsPage.map(notificationMapper::toDto);
        return ResponseEntity.ok(dtoPage);
    }

    // Mark a notification as read.
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponseDTO> markNotificationAsRead(@PathVariable Long notificationId) 
    {
        Notification notification = notificationService.markAsRead(notificationId);
        
        if(notification == null) 
        {
            return ResponseEntity.notFound().build();
        }
        
        NotificationResponseDTO responseDto = notificationMapper.toDto(notification);
        return ResponseEntity.ok(responseDto);
    }
}