package com.blockchain.accesscontrol.access_control_system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class Web3jConfig 
{
	@Value("${ganache.address}")
    private String ganacheAddress;

    @Bean
    public Web3j web3j() 
    {
        return Web3j.build(new HttpService(ganacheAddress));
    }
}
