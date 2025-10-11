package com.example.ofs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
	@Autowired
    private UserService userService;
    
    @DeleteMapping("/deletefile/{fileId}")
    public ResponseEntity<Boolean> login(@PathVariable Long fileId) {
    	Optional<User> u=userService.removeFile(fileId);
    	if(u.isEmpty())
    		return ResponseEntity.notFound().build();
    	
    	return ResponseEntity.ok(true);
    }

    
    @PostMapping("/login")
    public User login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }
    
    @GetMapping("/id/{username}")
    public Long getIdByUsername(@PathVariable String Username) {
    	return userService.findIdByUsername(Username);
    }


    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }
    
    @GetMapping("/{id}/files")
    public List<Long> getFiles(@PathVariable Long id) {
    	User u= userService.getUserById(id);
    	List<Long> l=u.getFileIds();
//    	if(u.getEmail().equals(mail))
    	return l;
    	
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/{id}/add-file/{fileId}")
    public User addFileToUser(@PathVariable Long id, @PathVariable Long fileId) {
        return userService.addFileToUser(id, fileId);
    }

    @PostMapping("/{id}/add-files")
    public User addFilesToUser(@PathVariable Long id, @RequestBody List<Long> fileIds) {
        return userService.addFilesToUser(id, fileIds);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
