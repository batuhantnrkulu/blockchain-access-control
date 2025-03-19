package com.blockchain.accesscontrol.access_control_system.exception;

public class UnauthorizedAccessException extends RuntimeException 
{
    public UnauthorizedAccessException(String message) 
    {
        super(message);
    }
}