package maoi.platforme.services;

import maoi.platforme.exception.StorageException;
import maoi.platforme.exception.StorageFileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

@Service
public class UploadFileService {


    public String uploadFile(MultipartFile file, String storageDir, Path rootLocation) throws StorageException {
        Date date = new Date();
        String newFileName = date.getTime() + "_" + file.getOriginalFilename();
        try {
            String uploadDir = rootLocation.toString() + "/" + storageDir;
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                try {
                    Files.createDirectories(uploadPath);
                } catch (IOException e) {
                    throw new StorageException("Could not initialize storage", e);
                }
            }
            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = Paths.get(uploadDir + "/" + newFileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                return filePath.getFileName().toString();
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    public Path load(String filename, String storageDir, Path rootLocation) {
        Path newRootLocation = Paths.get(rootLocation.toString() + "/" + storageDir);
        return newRootLocation.resolve(filename);
    }

    public Resource loadAsResource(String filename, String storageDir, Path rootLocation) throws StorageFileNotFoundException {
        try {
            Path file = load(filename, storageDir, rootLocation);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }

    }

    public void deleteFile(String filename, String storageDir, Path rootLocation) throws IOException {
        Path file = load(filename, storageDir, rootLocation);
        Resource resource = new UrlResource(file.toUri());
        FileSystemUtils.deleteRecursively(resource.getFile());
    }
}
