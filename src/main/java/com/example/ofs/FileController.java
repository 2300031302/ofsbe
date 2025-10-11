package com.example.ofs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/files")
public class FileController {	
    private final FileService fileService;
    
    @Autowired
	UserController u;
    
    @Autowired
    FileRepository fr;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }
    
  
    
    @GetMapping("/access/{id}")
    public ResponseEntity<Boolean> isAccess(@PathVariable Long id,@RequestParam String mail){
    	if(getFileMetadat(id).isPublic()||getFileMetadat(id).getAllowedUsers().contains(mail))
    		return ResponseEntity.ok(true);
    	return ResponseEntity.ok(false);
    }
    
    @GetMapping("/username/{username}/files")
    public ResponseEntity<List<FileEntity>> getFileByUsername(@PathVariable String username,@RequestParam String mail){
    	ResponseEntity<List<FileEntity>> l=this.getFileByUserMail(u.getIdByUsername(username));
    	//List<FileEntity> files=l.getBody();
    	if(u.getUser(u.getIdByUsername(username)).getEmail()!=mail) {
    		List<Long> list=u.getFiles(u.getIdByUsername(username));
    		List<FileEntity> files=new ArrayList<>();
    		for(Long i:list) {
    			FileEntity f=getFileMetadat(i);
    			if(f.isPublic()||f.getAllowedUsers().contains(mail))
    				files.add(getFileMetadat(i));
    		}
    		if(files.size()==0) return ResponseEntity.notFound().build();
    		return ResponseEntity.ok(files);
    		
    	}
    	return this.getFileByUserMail(u.getIdByUsername(username));
    }
    
    @GetMapping("/user/{id}/files")
    public ResponseEntity<List<FileEntity>> getFileByUserMail(@PathVariable Long id){
    	
    	List<Long> l=u.getFiles(id);
    	List<FileEntity> files=new ArrayList<>();
    	for(Long i:l) {
    		files.add(getFileMetadat(i));
    	}
    	if(files.size()==0)
    		ResponseEntity.notFound().build();
    	
    	return ResponseEntity.ok(files);
    }
    

    @PostMapping("/upload")
    public ResponseEntity<Long> uploadFile(@RequestParam("file") MultipartFile file,@RequestParam(value = "isPublic", defaultValue = "false") boolean isPublic,@RequestParam(value = "allowedUsers", required = false) List<String> allowedUsers) throws Exception {

    	String filename = file.getOriginalFilename();
    	String filetype = file.getContentType();
    	byte[] data = file.getBytes();

    	FileEntity saved = fileService.storeFile(filename, filetype, data, isPublic, allowedUsers);
    	return ResponseEntity.ok(saved.getId());
}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFile(
            @PathVariable Long id,
            @RequestParam(value = "email", required = false) String email) {

        fileService.deleteFile(id, email);
        return ResponseEntity.ok("File with id " + id + " deleted successfully");
    }
    
    @GetMapping
    public List<FileEntity> getAllFiles(){
    	return fr.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id,@RequestParam String mail) {
        FileEntity fileEntity = fileService.getFile(id,mail);
        if (fileEntity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header("Content-Type", fileEntity.getFileType())
                .header("Content-Disposition", "attachment; filename=\"" + fileEntity.getFileName() + "\"")
                .body(fileEntity.getData());
    }
    public FileEntity getFileMetadat(@PathVariable Long id) {
        FileEntity fileEntity = fileService.getFileById(id);
        if (fileEntity == null) {
            return null;
        }
        return fileEntity;
    }
 // In FileController
    @GetMapping("/{id}/meta")
    public ResponseEntity<FileEntity> getFileMetadata(@PathVariable Long id) {
        FileEntity fileEntity = fileService.getFileById(id);
        if (fileEntity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fileEntity);
    }

}
