package com.blockchain.accesscontrol.access_control_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.blockchain.accesscontrol.access_control_system.enums.Role;
import com.blockchain.accesscontrol.access_control_system.enums.Status;
import com.blockchain.accesscontrol.access_control_system.model.Peer;

@Repository
public interface PeerRepository extends JpaRepository<Peer, Long>
{
	@Query("SELECT p FROM Peer p WHERE p.role = :role AND p.bcStatus = :status")
	List<Peer> findAllByRoleAndStatus(@Param("role") Role role, @Param("status") Status status);

	@Query("SELECT COUNT(p) FROM Peer p WHERE p.role = :role AND p.bcStatus = :status")
	long countAllByRoleAndStatus(@Param("role") Role role, @Param("status") Status status);

    Optional<Peer> findByBcAddress(String address);
    
    Optional<Peer> findByUsername(String username);
    
    @Query("SELECT DISTINCT p.group FROM Peer p WHERE p.group IS NOT NULL")
    List<String> findAllDistinctGroups();

    List<Peer> findByGroupOrderByErc20TokenAmountDesc(String group);

    @Query("SELECT p FROM Peer p WHERE p.group = ?1 AND p.role = ?2")
    Peer findByGroupAndRole(String group, Role role);
}
