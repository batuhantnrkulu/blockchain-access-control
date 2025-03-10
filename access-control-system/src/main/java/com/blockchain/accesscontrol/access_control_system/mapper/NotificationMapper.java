package com.blockchain.accesscontrol.access_control_system.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.blockchain.accesscontrol.access_control_system.dto.responses.NotificationResponseDTO;
import com.blockchain.accesscontrol.access_control_system.model.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper 
{
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "formatDateTime")
    @Mapping(source = "isRead", target = "read")
    NotificationResponseDTO toDto(Notification notification);

    // Custom mapping method to format LocalDateTime
    @Named("formatDateTime")
    default String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null 
            ? dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) 
            : null;
    }
}
