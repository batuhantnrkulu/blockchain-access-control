package com.blockchain.accesscontrol.access_control_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.blockchain.accesscontrol.access_control_system.model.ValidatorList;

@Repository
public interface ValidatorListRepository extends JpaRepository<ValidatorList, Long> 
{
	@Query("SELECT v FROM ValidatorList v WHERE v.unjoinedPeer.id = :unjoinedPeerId AND v.approved = false")
    List<ValidatorList> findPendingValidatorsByUnjoinedPeerId(@Param("unjoinedPeerId") Long unjoinedPeerId);

    @Query("SELECT COUNT(v) FROM ValidatorList v WHERE v.unjoinedPeer.id = :unjoinedPeerId AND v.approved = true")
    long countApprovedValidators(@Param("unjoinedPeerId") Long unjoinedPeerId);
}