package restfulapi.spring.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import restfulapi.spring.web.payload.UploadFileResponse;
import restfulapi.spring.web.services.FileStorageService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class FileEmployeeController {

    private static final Logger log = LoggerFactory.getLogger(FileEmployeeController.class);
    private FileStorageService fileStorageService;

    @Autowired
    public FileEmployeeController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadfile/")
                .path(fileName)
                .toUriString();
        String fileType = file.getContentType();
        long fileSize = file.getSize();
        return new UploadFileResponse(fileName,fileDownloadUri,fileType,fileSize);
    }

//    @PostMapping("/uploadMultiplesFiles")
    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type");
        }

        if (contentType == null)
            contentType = "application/octet-stream";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+resource.getFilename()+"\"")
                .body(resource);
    }
}
