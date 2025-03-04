package com.blockchain.accesscontrol.access_control_system.utils;

import java.math.BigInteger;

import com.blockchain.accesscontrol.access_control_system.enums.Role;

public class RoleUtils 
{
	public static Role mapRole(BigInteger roleValue) 
	{
        int roleIndex = roleValue != null ? roleValue.intValue() : 0;
        
        if (roleIndex >= 0 && roleIndex < Role.values().length) 
        {
            return Role.values()[roleIndex];
        } 
        else 
        {
            return Role.NONE;  // Default role
        }
    }
	
	public static Role mapRole(String memberType) 
	{
	    try 
	    {
	        return Role.valueOf(memberType.toUpperCase());
	    } 
	    catch (IllegalArgumentException e) 
	    {
	        return Role.NONE;
	    }
	}
	
	public static long getTokenAmount(Role role) 
	{
	    switch (role) 
	    {
	        case PRIMARY_GROUP_HEAD:
	            return 1_000_000L;
	        case SECONDARY_GROUP_HEAD:
	            return 500_000L;
	        case REGULAR_MEMBER:
	            return 100_000L;
	        default:
	            return 0L; // Default case
	    }
	}  
}
