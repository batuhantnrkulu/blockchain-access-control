package com.blockchain.accesscontrol.access_control_system.mapper;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.blockchain.accesscontrol.access_control_system.contracts.RoleToken.Member;
import com.blockchain.accesscontrol.access_control_system.dto.responses.MemberDTO;
import com.blockchain.accesscontrol.access_control_system.enums.Role;
import com.blockchain.accesscontrol.access_control_system.enums.Status;
import com.blockchain.accesscontrol.access_control_system.model.Peer;

@Mapper(componentModel = "spring", imports = {Role.class}, builder = @Builder(disableBuilder = true))
public interface MemberMapper {

    @Mapping(source = "lastStatusUpdate", target = "lastStatusUpdate", qualifiedByName = "bigIntegerToDateTime")
    @Mapping(source = "role", target = "role", qualifiedByName = "mapRoleEnum")
    MemberDTO toMemberDTO(Member member);
    
    @Mapping(source = "bcAddress", target = "memberAddress")
    @Mapping(source = "username", target = "name")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "bcStatus", target = "status", qualifiedByName = "mapStatusToString")
    @Mapping(source = "bcLastStatusUpdate", target = "lastStatusUpdate")
    @Mapping(source = "group", target = "memberType")
    MemberDTO toMemberDTO(Peer peer);

    @Named("mapStatusToString")
    default String mapStatusToString(Status status) 
    {
        return status != null ? status.name() : null; //Converts enum to string: BENIGN, SUSPICIOUS, MALICIOUS)
    }
    
    // Custom mapping for BigInteger to LocalDateTime
    @Named("bigIntegerToDateTime")
    default LocalDateTime mapBigIntegerToDateTime(BigInteger value) 
    {
        return value != null ? 
               LocalDateTime.ofInstant(Instant.ofEpochSecond(value.longValue()), ZoneId.systemDefault()) : 
               null;
    }

    // Custom mapping for BigInteger to Role enum
    @Named("mapRoleEnum")
    default Role mapRoleEnum(BigInteger roleValue) {
        int roleIndex = roleValue != null ? roleValue.intValue() : 0;
        if (roleIndex >= 0 && roleIndex < Role.values().length) {
            return Role.values()[roleIndex];
        } else {
            return Role.NONE;  // Default role
        }
    }
}