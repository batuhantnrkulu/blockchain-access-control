package com.blockchain.accesscontrol.access_control_system.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.blockchain.accesscontrol.access_control_system.config.TransactionManagerFactory;
import com.blockchain.accesscontrol.access_control_system.contracts.AccessControlContract;
import com.blockchain.accesscontrol.access_control_system.dto.requests.PolicyBulkUpdateRequest;
import com.blockchain.accesscontrol.access_control_system.dto.responses.PolicyStatusResponse;
import com.blockchain.accesscontrol.access_control_system.enums.Action;
import com.blockchain.accesscontrol.access_control_system.enums.Permission;
import com.blockchain.accesscontrol.access_control_system.model.AccessRequest;
import com.blockchain.accesscontrol.access_control_system.model.Policy;
import com.blockchain.accesscontrol.access_control_system.model.Resource;
import com.blockchain.accesscontrol.access_control_system.repository.AccessRequestRepository;
import com.blockchain.accesscontrol.access_control_system.repository.PolicyRepository;
import com.blockchain.accesscontrol.access_control_system.repository.ResourceRepository;
import com.blockchain.accesscontrol.access_control_system.utils.EncryptionUtil;

import jakarta.transaction.Transactional;

@Service
public class PolicyService extends BaseContractService<AccessControlContract> 
{
    private final PolicyRepository policyRepository;
    private final AccessRequestRepository accessRequestRepository;
    private final ResourceRepository resourceRepository;
    private final EncryptionUtil encryptionUtil;

    public PolicyService(Web3j web3j, TransactionManagerFactory transactionManagerFactory, PolicyRepository policyRepository, AccessRequestRepository accessRequestRepository,
    		ResourceRepository resourceRepository, EncryptionUtil encryptionUtil) 
    {
    	super(web3j, transactionManagerFactory, "0x0");
        this.policyRepository = policyRepository;
        this.accessRequestRepository = accessRequestRepository;
        this.resourceRepository = resourceRepository;
        this.encryptionUtil = encryptionUtil;
    }
    
    /**
     * Calls the on-chain policyAdd function.
     */
    public TransactionReceipt addPolicyOnChain(String accAddress, String objectPeerPrivateKey, String resource, String action, String permission) throws Exception {
        AccessControlContract contract = loadContract(AccessControlContract.class, accAddress, objectPeerPrivateKey);
        return contract.policyAdd(resource, action, permission).send();
    }
    
    /**
     * Calls the on-chain policyUpdate function.
     */
    public TransactionReceipt updatePolicyOnChain(BigInteger policyId, String accAddress, String objectPeerPrivateKey, String resource, String action, String permission) throws Exception {
        AccessControlContract contract = loadContract(AccessControlContract.class, accAddress, objectPeerPrivateKey);
        return contract.policyUpdate(policyId, resource, action, permission).send();
    }
    
    /**
     * Calls the on-chain policyDelete function.
     */
    public TransactionReceipt deletePolicyOnChain(BigInteger policyId, String accAddress, String objectPeerPrivateKey) throws Exception {
        AccessControlContract contract = loadContract(AccessControlContract.class, accAddress, objectPeerPrivateKey);
        return contract.policyDelete(policyId).send();
    }

    /**
     * Processes a bulk update of policies for the specified access request and resource.
     * For each action (VIEW, EDIT, DELETE):
     * - If the payload value equals "allowed", then a policy record is created if one does not already exist.
     * - If the payload value equals "disallowed", then any existing policy is removed.
     *
     * @param request the bulk update request containing accessRequestId, resourceId, and the three action states.
     * @return A list of messages indicating the outcome for each action.
     */
    @Transactional
    public List<String> updatePolicies(PolicyBulkUpdateRequest request) {
        List<String> messages = new ArrayList<>();
        try {
            messages.add(updateSinglePolicy(request.getAccessRequestId(), request.getResourceId(), "VIEW", request.getView()));
            messages.add(updateSinglePolicy(request.getAccessRequestId(), request.getResourceId(), "EDIT", request.getEdit()));
            messages.add(updateSinglePolicy(request.getAccessRequestId(), request.getResourceId(), "DELETE", request.getDelete()));
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            messages.add("Error processing policy update: " + e.getMessage());
        }
        return messages;
    }

    private String updateSinglePolicy(Long accessRequestId, Long resourceId, String actionStr, String permissionState) throws Exception {
        // Fetch the AccessRequest record
        AccessRequest accessRequest = accessRequestRepository.findById(accessRequestId)
                .orElseThrow(() -> new RuntimeException("AccessRequest not found with id: " + accessRequestId));

        // Fetch the Resource record
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource not found with id: " + resourceId));

        // Convert the incoming action string to the enum (e.g., "VIEW", "EDIT", or "DELETE")
        Action action;
        try 
        {
            action = Action.valueOf(actionStr.toUpperCase());
        } 
        catch (IllegalArgumentException e) 
        {
            throw new RuntimeException("Invalid action provided: " + actionStr);
        }

        // Determine if the permission should be allowed
        boolean allow = "allowed".equalsIgnoreCase(permissionState);

        // Look up any existing policy for the given combination
        Optional<Policy> policyOpt = policyRepository.findByAccessRequestAndResourceAndAction(accessRequest, resource, action);

        // Retrieve on-chain parameters: the ACC address from the AccessRequest and
        // object peer's private key
        String accAddress = accessRequest.getAccAddress(); 
        String objectPeerPrivateKey = accessRequest.getObjectPeer().getPrivateKey(encryptionUtil);
        
        if (allow) {
            if (!policyOpt.isPresent()) {
                Policy newPolicy = new Policy();
                newPolicy.setAccessRequest(accessRequest);
                newPolicy.setResource(resource);
                newPolicy.setObjectPeer(resource.getPeer());
                newPolicy.setAction(action);
                newPolicy.setPermission(Permission.ALLOWED);
                policyRepository.save(newPolicy);

                // Call on-chain: add policy
                // Note: You may want to capture the on-chain policyId from the event or receipt and store it.
                addPolicyOnChain(accAddress, objectPeerPrivateKey,
                        resource.getResourceName(), action.name(), "allow");
            }
            return "Policy updated: action " + action + " allowed.";
        } else {
            if (policyOpt.isPresent()) {
                Policy existingPolicy = policyOpt.get();
                BigInteger policyId = BigInteger.valueOf(existingPolicy.getId());  
                policyRepository.delete(existingPolicy);

                // Call on-chain: delete policy
                deletePolicyOnChain(policyId, accAddress, objectPeerPrivateKey);

                return "Policy removed: action " + action + " disallowed.";
            }
            return "No policy to remove.";
        }
    }
    
    /**
     * Retrieves the current policy status for the specified access request and resource.
     * For each action (VIEW, EDIT, DELETE), if a policy exists the returned value is "allowed".
     * Otherwise, it is "disallowed".
     */
    @Transactional
    public PolicyStatusResponse getPolicyStatus(Long accessRequestId, Long resourceId) 
    {
        AccessRequest accessRequest = accessRequestRepository.findById(accessRequestId)
                .orElseThrow(() -> new RuntimeException("AccessRequest not found with id: " + accessRequestId));

        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource not found with id: " + resourceId));

        PolicyStatusResponse response = new PolicyStatusResponse();

        // Check VIEW permission
        Optional<Policy> viewPolicy = policyRepository.findByAccessRequestAndResourceAndAction(accessRequest, resource, Action.VIEW);
        response.setView(viewPolicy.isPresent() ? "allowed" : "denied");

        // Check EDIT permission
        Optional<Policy> editPolicy = policyRepository.findByAccessRequestAndResourceAndAction(accessRequest, resource, Action.EDIT);
        response.setEdit(editPolicy.isPresent() ? "allowed" : "denied");

        // Check DELETE permission
        Optional<Policy> deletePolicy = policyRepository.findByAccessRequestAndResourceAndAction(accessRequest, resource, Action.DELETE);
        response.setDelete(deletePolicy.isPresent() ? "allowed" : "denied");

        return response;
    }
}