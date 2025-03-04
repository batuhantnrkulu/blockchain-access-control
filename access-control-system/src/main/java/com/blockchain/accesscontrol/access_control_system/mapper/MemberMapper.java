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
import com.blockchain.accesscontrol.access_control_system.model.Peer;

@Mapper(componentModel = "spring", imports = {Role.class}, builder = @Builder(disableBuilder = true))
public interface MemberMapper {

    @Mapping(source = "lastStatusUpdate", target = "lastStatusUpdate", qualifiedByName = "bigIntegerToDateTime")
    @Mapping(source = "role", target = "role", qualifiedByName = "mapRoleEnum")
    MemberDTO toMemberDTO(Member member);
    
    MemberDTO toMemberDTO(Peer peer);

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