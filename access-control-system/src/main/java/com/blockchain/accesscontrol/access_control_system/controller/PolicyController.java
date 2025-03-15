package com.blockchain.accesscontrol.access_control_system.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blockchain.accesscontrol.access_control_system.dto.requests.PolicyBulkUpdateRequest;
import com.blockchain.accesscontrol.access_control_system.dto.responses.PolicyStatusResponse;
import com.blockchain.accesscontrol.access_control_system.repository.ResourceRepository;
import com.blockchain.accesscontrol.access_control_system.service.PolicyService;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private final PolicyService policyService;
    private final ResourceRepository resourceRepository;

    public PolicyController(PolicyService policyService, ResourceRepository resourceRepository) 
    {
        this.policyService = policyService;
        this.resourceRepository = resourceRepository;
    }

    /**
     * POST /api/policies/update-bulk
     *
     * Expects a JSON payload like:
     * {
     *   "accessRequestId": 123,
     *   "resourceId": 456,
     *   "view": "allowed",
     *   "edit": "disallowed",
     *   "delete": "allowed"
     * }
     *
     * This endpoint creates or removes policies based on the desired permission states.
     */
    @PostMapping("/update-bulk")
    public ResponseEntity<?> updatePolicies(@RequestBody PolicyBulkUpdateRequest request) {
    	try {
            List<String> result = policyService.updatePolicies(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace(); // Log error
            return ResponseEntity.status(500).body("Failed to update policies: " + e.getMessage());
        }
    }
    
    /**
     * GET /api/policies/status?accessRequestId={accessRequestId}&resourceId={resourceId}
     * Returns the current policy status for a given access request and resource.
     * The response JSON includes the state of "view", "edit", and "delete" permissions.
     */
    @GetMapping("/status")
    public ResponseEntity<PolicyStatusResponse> getPolicyStatus(@RequestParam Long accessRequestId, @RequestParam Long resourceId) 
    {
        PolicyStatusResponse response = policyService.getPolicyStatus(accessRequestId, resourceId);
        return ResponseEntity.ok(response);
    }
}
