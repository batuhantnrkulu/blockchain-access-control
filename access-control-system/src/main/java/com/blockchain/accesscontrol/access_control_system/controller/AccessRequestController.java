package com.blockchain.accesscontrol.access_control_system.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blockchain.accesscontrol.access_control_system.dto.requests.CreateAccessRequestDTO;
import com.blockchain.accesscontrol.access_control_system.dto.responses.AccessRequestResponse;
import com.blockchain.accesscontrol.access_control_system.mapper.AccessRequestMapper;
import com.blockchain.accesscontrol.access_control_system.model.AccessRequest;
import com.blockchain.accesscontrol.access_control_system.service.AccessRequestService;

@RestController
@RequestMapping("/api/access-request")
public class AccessRequestController 
{
	private final AccessRequestService accessRequestService;
	private final AccessRequestMapper accessRequestMapper;
	
	public AccessRequestController(AccessRequestService accessRequestService,
			AccessRequestMapper accessRequestMapper) 
	{
		this.accessRequestService = accessRequestService;
		this.accessRequestMapper = accessRequestMapper;
	}

	@PostMapping("/create")
	public ResponseEntity<?> createAccessRequest(@RequestBody CreateAccessRequestDTO requestDto) 
	{
		try 
		{
			AccessRequest accessRequest = accessRequestService.createAccessRequest(requestDto.getObjectPeerId(),
					requestDto.getSubjectPeerId());
			
			// Map the entity to a response DTO
            AccessRequestResponse response = accessRequestMapper.toResponse(accessRequest);
            return ResponseEntity.ok(response);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to create access request: " + e.getMessage());
		}
	}

	@PostMapping("/approve/{id}")
	public ResponseEntity<?> approveAccessRequest(@PathVariable("id") Long requestId,
			@RequestParam("accType") String accType) 
	{
		try 
		{
			String accAddress = accessRequestService.approveAccessRequest(requestId, accType);
			return ResponseEntity.ok("Access approved; ACC deployed at address: " + accAddress);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error approving access request: " + e.getMessage());
		}
	}
	
	@GetMapping("/my-requests")
	public ResponseEntity<List<AccessRequestResponse>> getMyRequests(@RequestParam String username) 
	{
        List<AccessRequestResponse> responseList = accessRequestService.getRequestsICreated(username)
                .stream()
                .map(accessRequestMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/requests-to-me")
    public ResponseEntity<List<AccessRequestResponse>> getRequestsToMe(@RequestParam String username) 
    {
    	List<AccessRequestResponse> responseList = accessRequestService.getRequestsToMe(username)
                .stream()
                .map(accessRequestMapper::toResponse)
                .collect(Collectors.toList());
    	
    	return ResponseEntity.ok(responseList);

    }
}
