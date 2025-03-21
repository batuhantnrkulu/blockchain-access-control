package com.blockchain.accesscontrol.access_control_system.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blockchain.accesscontrol.access_control_system.dto.responses.ResourceResponseDTO;
import com.blockchain.accesscontrol.access_control_system.mapper.ResourceMapper;
import com.blockchain.accesscontrol.access_control_system.model.Resource;
import com.blockchain.accesscontrol.access_control_system.service.ResourceService;
import com.blockchain.accesscontrol.access_control_system.utils.ByteArrayMultipartFile;
import com.blockchain.accesscontrol.access_control_system.utils.EncryptionUtil;

@RestController
@RequestMapping("/api/resources")
public class ResourceController 
{
    private final ResourceService resourceService;
    private final ResourceMapper resourceMapper;
    private final EncryptionUtil encryptionUtil;

    public ResourceController(ResourceService resourceService, ResourceMapper resourceMapper, EncryptionUtil encryptionUtil) 
    {
        this.resourceService = resourceService;
        this.resourceMapper = resourceMapper;
        this.encryptionUtil = encryptionUtil;
    }

    /**
     * Creates a new resource.
     * The request does not contain a 'peer' in the JSON payload.
     * Instead, the username is provided as a query parameter.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResourceResponseDTO> createResource(
            @RequestParam String username,
            @RequestParam String resourceName,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "dataResource", required = false) String dataResource) 
    {
        try 
        {
            if (file != null) 
            {
                ByteArrayOutputStream encryptedFileOutputStream = new ByteArrayOutputStream();
                encryptionUtil.encryptFile(file.getInputStream(), encryptedFileOutputStream);
                byte[] encryptedFileBytes = encryptedFileOutputStream.toByteArray();
                
                file = new ByteArrayMultipartFile(encryptedFileBytes, file.getOriginalFilename(), file.getContentType());
            }
            
            if (dataResource != null) 
            {
                dataResource = encryptionUtil.encrypt(dataResource);
            }
            
            Resource resource = resourceService.createResource(username, resourceName, file, dataResource);
            ResourceResponseDTO responseDTO = resourceMapper.resourceToResourceResponseDTO(resource);
            return ResponseEntity.ok(responseDTO);
        } 
        catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Retrieves a paginated list of resources for the given user.
     * Optional parameters: search, page, and size.
     * The DTO returned includes only the required fields.
     */
    @GetMapping
    public ResponseEntity<Page<ResourceResponseDTO>> getResources(
            @RequestParam String username, @RequestParam(required = false) String search, Pageable pageable) 
    {
        Page<Resource> resourcesPage = resourceService.getResourcesForPeer(username, search, pageable);
        
        // Map each Resource to a DTO.
        Page<ResourceResponseDTO> dtoPage = resourcesPage.map(resourceMapper::resourceToResourceResponseDTO);
        return ResponseEntity.ok(dtoPage);
    }

    /**
     * Updates an existing resource.
     * The client sends resource fields in the request body using the DTO.
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResourceResponseDTO> updateResource(
            @PathVariable Long id,
            @RequestParam String resourceName,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "dataResource", required = false) String dataResource) 
    {
        try 
        {    
            if (file != null) 
            {
                ByteArrayOutputStream encryptedFileOutputStream = new ByteArrayOutputStream();
                encryptionUtil.encryptFile(file.getInputStream(), encryptedFileOutputStream);
                byte[] encryptedFileBytes = encryptedFileOutputStream.toByteArray();
                file = new ByteArrayMultipartFile(encryptedFileBytes, file.getOriginalFilename(), file.getContentType());
            }
            
            if (dataResource != null) 
            {
                dataResource = encryptionUtil.encrypt(dataResource);
            }
            
            Resource updatedResource = resourceService.updateResource(id, resourceName, file, dataResource);
            ResourceResponseDTO responseDTO = resourceMapper.resourceToResourceResponseDTO(updatedResource);
            return ResponseEntity.ok(responseDTO);
        } 
        catch (Exception e) 
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Deletes the resource corresponding to the given id.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) 
    {
        resourceService.deleteResource(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Retrieves stored JPG images.
     */
    @GetMapping("/image")
    public ResponseEntity<org.springframework.core.io.Resource> getImage(@RequestParam("path") String path) throws IOException 
    {
        try {
            Path imagePath = Paths.get(path);
            ByteArrayOutputStream decryptedFileOutputStream = new ByteArrayOutputStream();
            encryptionUtil.decryptFile(Files.newInputStream(imagePath), decryptedFileOutputStream);
            byte[] decryptedFileBytes = decryptedFileOutputStream.toByteArray();
            org.springframework.core.io.Resource resource = new ByteArrayResource(decryptedFileBytes);

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Retrieves numeric data for a resource.
     */
    @GetMapping("/numeric-data")
    public ResponseEntity<String> getNumericData(@RequestParam("id") Long id) 
    {
        try {
            String dataResource = resourceService.getNumericValue(id);
            if (dataResource != null) {
                dataResource = encryptionUtil.decrypt(dataResource);
            }
            return ResponseEntity.ok().body(dataResource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
