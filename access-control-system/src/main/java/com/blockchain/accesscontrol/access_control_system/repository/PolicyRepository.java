package com.blockchain.accesscontrol.access_control_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blockchain.accesscontrol.access_control_system.enums.Action;
import com.blockchain.accesscontrol.access_control_system.model.AccessRequest;
import com.blockchain.accesscontrol.access_control_system.model.Policy;
import com.blockchain.accesscontrol.access_control_system.model.Resource;

public interface PolicyRepository extends JpaRepository<Policy, Long> 
{
    List<Policy> findByAccessRequestAndResource(AccessRequest accessRequest, Resource resource);
    
    Optional<Policy> findByAccessRequestAndResourceAndAction(AccessRequest accessRequest, Resource resource, Action action);
}
