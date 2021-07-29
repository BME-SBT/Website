package hu.schdesign.solarboat.service;



import hu.schdesign.solarboat.Exceptions.FileStorageException;
import hu.schdesign.solarboat.Exceptions.MyFileNotFoundException;
import hu.schdesign.solarboat.FileStorageProperties;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Iterator;

@Service
public class FileStorageService {

    private Path fileStorageLocation;
    private final String path;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.path = fileStorageProperties.getUploadDirectory();
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDirectory())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public MultipartFile resizeImage(MultipartFile file, String path, int width) throws IOException {
        if (!file.getContentType().contains("image")) {
            throw new RuntimeException("Rossz fájlformátum!");
        }
        BufferedImage tempImage = ImageIO.read(file.getInputStream());
        BufferedImage originalImage = new BufferedImage(tempImage.getWidth(null), tempImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
        InputStream is = file.getInputStream();
        Image image = ImageIO.read(is);
        originalImage.getGraphics().drawImage(image, 0, 0 , null);
        originalImage.getGraphics().dispose();
        if (originalImage.getWidth() > width) {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            String ext = file.getContentType().contains("png") ? "png" : "jpg";
            ImageIO.write(Scalr.resize(originalImage, width), ext, baos);
            baos.flush();
            MultipartFile newFile = new MockMultipartFile(file.getName(), file.getOriginalFilename(), file.getContentType(), baos.toByteArray());
            return newFile;
        }
        return file;
    }

    public String storeResizedFile(MultipartFile file, String path, String name) {
        if (!file.getContentType().contains("image")) {
            return null;
        }
        this.fileStorageLocation = Paths.get(this.path + "/" + path)
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
        // Normalize file name
        String concatFilename = FilenameUtils.removeExtension(file.getOriginalFilename());
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if(concatFilename.isEmpty()){
            concatFilename = "";
        }
        if (!name.isEmpty()) {
            concatFilename = concatFilename + "_" + name;
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        concatFilename = concatFilename + "_" + timestamp.getTime() + "." + extension;
        System.out.println(concatFilename);
        String fileName = StringUtils.cleanPath(concatFilename);

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public String storeFile(MultipartFile file, String path) {
        if (!file.getContentType().contains("image")) {
            return null;
        }
        this.fileStorageLocation = Paths.get(this.path + "/" + path)
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
        // Normalize file name
        String concatFilename = file.getOriginalFilename();
        if (concatFilename.isEmpty()) {
            concatFilename = "";
        }
        //concatFilename += LocalDateTime.now().toString();
        String fileName = StringUtils.cleanPath(concatFilename);

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    public void deleteFile(String fileName, String path) {
        try {
            Path filePath = Paths.get(this.path + "/" + path + "/" + fileName)
                    .toAbsolutePath().normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                File file = new File(filePath.toString());
                boolean b = file.delete();

            }
//            Ha nem létezik a fájl akkor simán törölhető az adatbázisból
//            else {
//                throw new MyFileNotFoundException("File not found " + fileName);
//            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
}
