package com.blockchain.accesscontrol.access_control_system.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blockchain.accesscontrol.access_control_system.dto.requests.ValidatorRequestDTO;
import com.blockchain.accesscontrol.access_control_system.model.ValidatorList;
import com.blockchain.accesscontrol.access_control_system.repository.ValidatorListRepository;
import com.blockchain.accesscontrol.access_control_system.scheduler.ValidatorExpiryService;

@RestController
@RequestMapping("/api/validator-response")
public class ValidatorResponseController 
{
    private final ValidatorListRepository validatorListRepository;
    private final ValidatorExpiryService validatorExpiryService;

    public ValidatorResponseController(ValidatorListRepository validatorListRepository, ValidatorExpiryService validatorExpiryService) 
    {
        this.validatorListRepository = validatorListRepository;
        this.validatorExpiryService = validatorExpiryService;
    }

    @PostMapping
    public ResponseEntity<?> submitValidatorResponse(@RequestBody ValidatorRequestDTO request) 
    {
    	return validatorExpiryService.submitResponse(request);
    }
}
