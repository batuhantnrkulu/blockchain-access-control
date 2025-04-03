package com.blockchain.accesscontrol.access_control_system.scheduler;

import java.math.BigInteger;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.blockchain.accesscontrol.access_control_system.enums.Role;
import com.blockchain.accesscontrol.access_control_system.model.Peer;
import com.blockchain.accesscontrol.access_control_system.repository.PeerRepository;
import com.blockchain.accesscontrol.access_control_system.service.RoleTokenService;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

@Service
public class RoleSchedulerService 
{
	private static final Logger logger = LoggerFactory.getLogger(RoleSchedulerService.class);
    
    private final MeterRegistry meterRegistry;
    private final PeerRepository peerRepository;
    private final RoleTokenService roleTokenService;

    public RoleSchedulerService(PeerRepository peerRepository, RoleTokenService roleTokenService, MeterRegistry meterRegistry) 
    {
        this.peerRepository = peerRepository;
        this.roleTokenService = roleTokenService;
        this.meterRegistry = meterRegistry;
    }

    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    public void updateRolesBasedOnTokens() 
    {
    	Timer.Sample sample = Timer.start(meterRegistry);
        
        try {
            List<String> groups = peerRepository.findAllDistinctGroups();
            meterRegistry.gauge("scheduler.groups.count", groups.size());
            
            groups.forEach(group -> {
                updateGroupRoles(group);
            });
            
            sample.stop(meterRegistry.timer("scheduler.execution.time"));
        } catch (Exception e) {
            meterRegistry.counter("scheduler.errors").increment();
            logger.error("Scheduler failed", e);
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
        	Timer.Sample swapTimer = Timer.start(meterRegistry);
        	
        	try 
        	{
        		roleTokenService.swapRoles(currentPrimary.getBcAddress(), expectedPrimary.getBcAddress());
                
                // Record latency
                swapTimer.stop(meterRegistry.timer("scheduler.swap.latency"));
                
                // Record successful swap
                meterRegistry.counter("scheduler.swaps", "type", "primary").increment();
        	} 
        	catch (RuntimeException ex) 
        	{
        		meterRegistry.counter("scheduler.swap.errors", "type", "primary").increment();
                logger.error("Primary role swap failed", ex);
        	}
        }
    }

    private void handleSecondaryRoleUpdate(Peer currentSecondary, Peer expectedSecondary, String group) 
    {
        if (currentSecondary == null || !currentSecondary.getId().equals(expectedSecondary.getId())) 
        {
        	Timer.Sample swapTimer = Timer.start(meterRegistry);
        	
        	try 
        	{
        	    roleTokenService.swapRoles(currentSecondary.getBcAddress(), expectedSecondary.getBcAddress());
        	    
        	    // Record latency
                swapTimer.stop(meterRegistry.timer("scheduler.swap.latency"));
                
                // Record successful swap
                meterRegistry.counter("scheduler.swaps", "type", "secondary").increment();
        	} 
        	catch (RuntimeException ex) 
        	{
        		meterRegistry.counter("scheduler.swap.errors", "type", "secondary").increment();
                logger.error("Primary role swap failed", ex);
        	}
        }
    }
}