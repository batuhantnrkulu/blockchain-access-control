package com.blockchain.accesscontrol.access_control_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blockchain.accesscontrol.access_control_system.dto.requests.ValidatorRequestDTO;
import com.blockchain.accesscontrol.access_control_system.repository.ValidatorListRepository;
import com.blockchain.accesscontrol.access_control_system.scheduler.ValidatorExpiryService;

@RestController
@RequestMapping("/api/validator-response")
public class ValidatorResponseController 
{
    private final ValidatorExpiryService validatorExpiryService;

    public ValidatorResponseController(ValidatorExpiryService validatorExpiryService) 
    {
        this.validatorExpiryService = validatorExpiryService;
    }

    @PostMapping
    public ResponseEntity<?> submitValidatorResponse(@RequestBody ValidatorRequestDTO request) 
    {
    	return validatorExpiryService.submitResponse(request);
    }
    
    /**
     * API Endpoint to check validation status.
     * @param unjoinedPeerId ID of the unjoined peer.
     * @param validatorId ID of the validator.
     * @return Boolean value (false if expired, true if responded, null if pending).
     */
    @GetMapping("/check-response")
    public ResponseEntity<Boolean> hasUserResponded(
            @RequestParam Long unjoinedPeerId,
            @RequestParam Long validatorId) 
    {
        Boolean result = validatorExpiryService.checkValidationStatus(unjoinedPeerId, validatorId);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }
}
