package com.blockchain.accesscontrol.access_control_system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer 
{
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) 
    {
        // Clients can subscribe to "/topic" for notifications
        config.enableSimpleBroker("/topic");
        // The prefix for messages that are bound for methods annotated with @MessageMapping
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) 
    {
        // A WebSocket endpoint that clients connect to; SockJS fallback is enabled for wide browser support
        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3000").withSockJS();
    }
}
