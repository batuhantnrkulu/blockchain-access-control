package com.blockchain.accesscontrol.access_control_system.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.blockchain.accesscontrol.access_control_system.model.Peer;
import com.blockchain.accesscontrol.access_control_system.model.Resource;
import com.blockchain.accesscontrol.access_control_system.repository.PeerRepository;
import com.blockchain.accesscontrol.access_control_system.repository.ResourceRepository;

@Service
public class ResourceService 
{
	private final ResourceRepository resourceRepository;
	private final PeerRepository peerRepository;

    private final String uploadDir = "D:/Desktop/Dersler/2024/thesis/images";

	public ResourceService(ResourceRepository resourceRepository, PeerRepository peerRepository) 
	{
        this.resourceRepository = resourceRepository;
        this.peerRepository = peerRepository;
    }

    public Resource createResource(String username, String resourceName, 
    		MultipartFile file, String dataResource) throws IOException 
    {
    	Peer peer = peerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Peer not found"));

        String filePath = null;
        
        if (file != null && !file.isEmpty()) 
        {
            filePath = saveFileLocally(file);
        }

        Resource resource = new Resource();
        resource.setResourceName(resourceName);
        resource.setJpgResource(filePath);
        resource.setDataResource(dataResource);
        resource.setPeer(peer);

        return resourceRepository.save(resource);
    }

    public Page<Resource> getResourcesForPeer(String username, String search, Pageable pageable) 
    {
        Peer peer = peerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Peer not found"));

        if (search != null && !search.isEmpty()) 
        {
            return resourceRepository.findByPeerAndResourceNameContainingIgnoreCase(peer, search, pageable);
        }

        return resourceRepository.findByPeer(peer, pageable);
    }

    public Resource updateResource(Long id, String resourceName, 
    		MultipartFile file, String dataResource) throws IOException 
    {
        Resource existing = resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        existing.setResourceName(resourceName);
        existing.setDataResource(dataResource);

        if (file != null && !file.isEmpty()) 
        {
            // Delete old file if exists
            if (existing.getJpgResource() != null) 
            {
                deleteFile(existing.getJpgResource());
            }

            // Save new file
            String filePath = saveFileLocally(file);
            existing.setJpgResource(filePath);
        }

        return resourceRepository.save(existing);
    }

    public void deleteResource(Long id) 
    {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        if (resource.getJpgResource() != null) 
        {
            deleteFile(resource.getJpgResource());
        }

        resourceRepository.deleteById(id);
    }

    private String saveFileLocally(MultipartFile file) throws IOException 
    {
        File directory = new File(uploadDir);
        
        if (!directory.exists()) 
        {
            directory.mkdirs(); // Create directory if not exists
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.write(filePath, file.getBytes());

        return filePath.toString(); // Save file path in DB
    }

    private void deleteFile(String filePath) 
    {
        File file = new File(filePath);
        if (file.exists()) 
        {
            file.delete();
        }
    }
}
