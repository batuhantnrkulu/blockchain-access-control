package com.blockchain.accesscontrol.access_control_system.scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.blockchain.accesscontrol.access_control_system.enums.Role;
import com.blockchain.accesscontrol.access_control_system.model.Peer;
import com.blockchain.accesscontrol.access_control_system.repository.PeerRepository;
import com.blockchain.accesscontrol.access_control_system.service.RoleTokenService;

@Service
public class RoleSchedulerService {

    private final PeerRepository peerRepository;
    private final RoleTokenService roleTokenService;

    public RoleSchedulerService(PeerRepository peerRepository, RoleTokenService roleTokenService) 
    {
        this.peerRepository = peerRepository;
        this.roleTokenService = roleTokenService;
    }

    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    public void updateRolesBasedOnTokens() 
    {
        List<String> groups = peerRepository.findAllDistinctGroups();
        
        for (String group : groups) 
        {
            updateGroupRoles(group);
        }
    }

    private void updateGroupRoles(String group) 
    {
        List<Peer> sortedPeers = peerRepository.findByGroupOrderByErc20TokenAmountDesc(group);
        
        if (sortedPeers.size() < 2) return;

        Peer expectedPrimary = sortedPeers.get(0);
        Peer expectedSecondary = sortedPeers.get(1);

        Peer currentPrimary = peerRepository.findByGroupAndRole(group, Role.PRIMARY_GROUP_HEAD);
        handlePrimaryRoleUpdate(currentPrimary, expectedPrimary);
        
        Peer currentSecondary = peerRepository.findByGroupAndRole(group, Role.SECONDARY_GROUP_HEAD);
        handleSecondaryRoleUpdate(currentSecondary, expectedSecondary, group);
    }

    private void handlePrimaryRoleUpdate(Peer currentPrimary, Peer expectedPrimary) 
    {
        if (currentPrimary == null || !currentPrimary.getId().equals(expectedPrimary.getId())) 
        {
        	try 
        	{
        	    roleTokenService.swapRoles(currentPrimary.getBcAddress(), expectedPrimary.getBcAddress());
        	} 
        	catch (RuntimeException ex) 
        	{
        	    System.out.println(ex);
        	}
        }
    }

    private void handleSecondaryRoleUpdate(Peer currentSecondary, Peer expectedSecondary, String group) 
    {
        if (currentSecondary == null || !currentSecondary.getId().equals(expectedSecondary.getId())) 
        {
        	try 
        	{
        	    roleTokenService.swapRoles(currentSecondary.getBcAddress(), expectedSecondary.getBcAddress());
        	} 
        	catch (RuntimeException ex) 
        	{
        	    System.out.println(ex);
        	}
        }
    }
}