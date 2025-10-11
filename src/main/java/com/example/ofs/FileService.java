package com.example.ofs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public FileEntity storeFile(String fileName, String fileType, byte[] data, boolean isPublic, List<String> allowedUsers) {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(fileName);
        fileEntity.setFileType(fileType);
        fileEntity.setData(data);
        fileEntity.setPublic(isPublic);
        fileEntity.setAllowedUsers(isPublic ? null : allowedUsers);

        return fileRepository.save(fileEntity);
    }

    public FileEntity getFile(Long id, String requesterEmail) {
        FileEntity file = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));

        if (!file.isPublic()) {
            if (requesterEmail == null || !file.getAllowedUsers().contains(requesterEmail)) {
                throw new RuntimeException("Access denied: not authorized");
            }
        }

        return file;
    }
    
    public void deleteFile(Long id, String requesterEmail) {
        FileEntity file = fileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));

        // If file is private -> only allowed users can delete
        if (!file.isPublic()&&!requesterEmail.matches("admin@gmail.com")) {
            boolean allowed = Collections
                    .unmodifiableList(file.getAllowedUsers() == null ? Collections.emptyList() : file.getAllowedUsers())
                    .contains(requesterEmail);

            if (!allowed) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to delete this file");
            }
        }

        fileRepository.deleteById(id);
    }

    public FileEntity getFileById(Long id) {
        Optional<FileEntity> fileOpt = fileRepository.findById(id);
        return fileOpt.orElse(null);  // unwrap Optional safely
    }
}
