package hu.schdesign.solarboat.api;

import hu.schdesign.solarboat.Exceptions.CustomMessageApiException;
import hu.schdesign.solarboat.UploadFileResponse;
import hu.schdesign.solarboat.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private FileStorageService fileStorageService;

    @Autowired
    FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("files/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName, "files");

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    @Secured("ROLE_USER")
    @PostMapping("/api/file/uploadFile")
    public ResponseEntity<UploadFileResponse> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) throws URISyntaxException, IOException {
        System.out.println(path);
        String fileName;
        String[] returnValues;
        if (path.equals("file")) {
            returnValues = fileStorageService.storeFile(file);
        } else {
            returnValues = fileStorageService.storeImage(file, path);
        }
        fileName = returnValues[0];

        if (fileName == null) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setLocation(new URI("/uploadFile"));
            responseHeaders.set("Error", "Error");
            return new ResponseEntity<UploadFileResponse>(null, responseHeaders, HttpStatus.BAD_REQUEST);
        }

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/")
                .path(fileName)
                .toUriString();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(new URI("uploadFile"));

        return new ResponseEntity<UploadFileResponse>(new UploadFileResponse(fileName,  fileDownloadUri,
                file.getContentType(), file.getSize(), returnValues[2], returnValues[1]), responseHeaders, HttpStatus.CREATED);
    }


    @PostMapping("/api/file/uploadMultipleFiles")
    public List<ResponseEntity<UploadFileResponse>> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files, @RequestParam("path") String path) {
        return Arrays.asList(files)
                .stream()
                .map(file -> {
                    try {
                        return uploadFile(file, path);
                    } catch (URISyntaxException e) {
                        throw new CustomMessageApiException("Nem megengedett fájlformátum");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

//    @Secured({"ROLE_USER", "ROLE_ADMIN"})
//    @GetMapping("/downloadFile/{fileName:.+}")
//    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
//        // Load file as Resource
//        Resource resource = fileStorageService.loadFileAsResource(fileName);
//
//        // Try to determine file's content type
//        String contentType = null;
//        try {
//            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
//        } catch (IOException ex) {
//            logger.info("Could not determine file type.");
//        }
//
//        // Fallback to the default content type if type could not be determined
//        if (contentType == null) {
//            contentType = "application/octet-stream";
//        }
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                .body(resource);
//    }

    @Secured("ROLE_USER")
    @DeleteMapping(path = "/api/file/deleteFile")
    public void deleteFile(@RequestBody String fileName) {
        // this.fileStorageService.deleteFile(fileName);
    }
}
