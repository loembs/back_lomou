package ism.atelier.atelier.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {
    String uploadFile(MultipartFile file);
    List<String> uploadMultipleFiles(List<MultipartFile> files);
}
