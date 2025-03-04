package com.blockchain.accesscontrol.access_control_system.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Access_Requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequest 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "object_id", nullable = false)
    private Peer objectPeer;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Peer subjectPeer;

    @Column(nullable = false)
    private Boolean approved = false;

    @Column(name = "acc_address")
    private String accAddress;
}
