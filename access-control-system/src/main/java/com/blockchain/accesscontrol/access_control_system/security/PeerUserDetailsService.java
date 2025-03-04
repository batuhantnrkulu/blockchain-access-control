package com.blockchain.accesscontrol.access_control_system.security;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.blockchain.accesscontrol.access_control_system.model.Peer;
import com.blockchain.accesscontrol.access_control_system.repository.PeerRepository;

public class PeerUserDetailsService implements UserDetailsService
{
	private final PeerRepository peerRepository;

    public PeerUserDetailsService(PeerRepository peerRepository) 
    {
        this.peerRepository = peerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
    {
        Optional<Peer> optPeer = peerRepository.findByUsername(username);
        if (!optPeer.isPresent()) {
            throw new UsernameNotFoundException("Peer not found with username: " + username);
        }
        
        Peer peer = optPeer.get();
        // In this example, every peer gets a role ROLE_PEER.
        return new User(peer.getUsername(), peer.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_PEER"))
        );
    }
	
}
