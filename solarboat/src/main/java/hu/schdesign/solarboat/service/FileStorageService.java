package hu.schdesign.solarboat.service;


import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import hu.schdesign.solarboat.Exceptions.FileStorageException;
import hu.schdesign.solarboat.Exceptions.MyFileNotFoundException;
import hu.schdesign.solarboat.FileStorageProperties;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.Objects;

@Service
public class FileStorageService {

    private Path fileStorageLocation;
    private final String path;
    private final int PICTURE_WIDTH = 1920;
    private final int SMALL_PICTURE_WIDTH = 480;

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

    public MultipartFile resizeImage(MultipartFile file, String path, int size) throws IOException {
        if (!Objects.requireNonNull(file.getContentType()).contains("image")) {
            throw new RuntimeException("Rossz fájlformátum!");
        }
        BufferedImage tempImage = ImageIO.read(file.getInputStream());
        BufferedImage originalImage = new BufferedImage(tempImage.getWidth(null), tempImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
        InputStream is = file.getInputStream();
        Image image = ImageIO.read(is);
        originalImage.getGraphics().drawImage(image, 0, 0, null);
        originalImage.getGraphics().dispose();

        try {
            final Metadata metadata = ImageMetadataReader.readMetadata(file.getInputStream());
            ExifIFD0Directory exifIFD0 = metadata.getDirectory(ExifIFD0Directory.class);
            Scalr.Rotation rotation = null;
            if (exifIFD0 != null) {
                int orientation = exifIFD0.getInt(ExifIFD0Directory.TAG_ORIENTATION);

                switch (orientation) {
                    case 1: // [Exif IFD0] Orientation - Top, left side (Horizontal / normal)
                        rotation = null;
                        break;
                    case 6: // [Exif IFD0] Orientation - Right side, top (Rotate 90 CW)
                        rotation = Scalr.Rotation.CW_90;
                        break;
                    case 3: // [Exif IFD0] Orientation - Bottom, right side (Rotate 180)
                        rotation = Scalr.Rotation.CW_180;
                        break;
                    case 8: // [Exif IFD0] Orientation - Left side, bottom (Rotate 270 CW)
                        rotation = Scalr.Rotation.CW_270;
                        break;
                }
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            String ext = file.getContentType().contains("png") ? "png" : "jpg";
            BufferedImage rotatedImage = originalImage;
            if (rotation != null) {
                rotatedImage = Scalr.rotate(originalImage, rotation);
            }
            if(size == SMALL_PICTURE_WIDTH){

                if ( rotatedImage.getWidth() > size) {
                    ImageIO.write(Scalr.resize(rotatedImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, size), ext, baos);
                } else {
                    ImageIO.write(rotatedImage, ext, baos);
                }
            }
            else{

                if (rotatedImage.getHeight() > rotatedImage.getWidth() && rotatedImage.getHeight() > size) {
                    ImageIO.write(Scalr.resize(rotatedImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, size, size), ext, baos);
                } else if (rotatedImage.getHeight() < rotatedImage.getWidth() && rotatedImage.getWidth() > size) {
                    ImageIO.write(Scalr.resize(rotatedImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, size), ext, baos);
                } else {
                    ImageIO.write(rotatedImage, ext, baos);
                }
            }
            baos.flush();
            MultipartFile newFile = new MockMultipartFile(file.getName(), file.getOriginalFilename(), file.getContentType(), baos.toByteArray());
            return newFile;

        } catch (ImageProcessingException | MetadataException e) {
            e.printStackTrace();
        }
        return file;
    }

    public String[] storeResizedFile(MultipartFile file, String path, String name) {
        if (!file.getContentType().contains("image")) {
            throw new RuntimeException("Rossz fájlformátum!");
        }
        Path fullPath = Paths.get(this.path + "/" + path)
                .toAbsolutePath().normalize();
//        this.fileStorageLocation = Paths.get(this.path + "/" + path)
//                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(fullPath);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
        // Normalize file name
        String concatFilename = "";
        concatFilename = FilenameUtils.removeExtension(file.getOriginalFilename());
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!name.isEmpty()) {
            concatFilename = concatFilename + "_" + name;
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        concatFilename = concatFilename + "." + extension;
//        concatFilename = concatFilename + "_" + timestamp.getTime() + "." + extension;
//        System.out.println(concatFilename);
        String fileName = StringUtils.cleanPath(concatFilename);

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = fullPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String[] returnString = new String[3];
            returnString[0] = fileName;
            returnString[1] = this.fileStorageLocation.toString();
            returnString[2] = targetLocation.toString();

            return returnString;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public String[] storeImage(MultipartFile file, String path) {
        if (!file.getContentType().contains("image")) {
            throw new RuntimeException("Rossz fájlformátum!");
        }
        Path fullPath = Paths.get(this.path + "/" + path)
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(fullPath);
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
            Path targetLocation = fullPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String[] returnString = new String[3];
            returnString[0] = fileName;
            returnString[1] = this.fileStorageLocation.toString();
            returnString[2] = targetLocation.toString();
            return returnString;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public String[] storeFile(MultipartFile file) {

        Path path = Paths.get(this.path + "/files")
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(path);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
        // Normalize file name
        String fileName = file.getOriginalFilename();
        if (fileName.isEmpty()) {
            fileName = "";
        }
        fileName = fileName.replace(' ', '_');
        fileName = fileName.replace('á', 'a');
        fileName = fileName.replace('é', 'e');
        fileName = fileName.replace('ó', 'o');
        fileName = fileName.replace('ő', 'o');
        fileName = fileName.replace('ö', 'o');
        fileName = fileName.replace('ü', 'u');
        fileName = fileName.replace('ű', 'u');
        fileName = fileName.replace('ú', 'u');
        fileName = fileName.replace('í', 'i');
        fileName = StringUtils.cleanPath(fileName);

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = path.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String[] returnString = new String[3];
            returnString[0] = fileName;
            returnString[1] = this.fileStorageLocation.toString();
            returnString[2] = targetLocation.toString();
            return returnString;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName, String path) {
        try {
            Path fullPath = Paths.get(this.path + "/" + path)
                    .toAbsolutePath().normalize();
            Path filePath = fullPath.resolve(fileName).normalize();
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
