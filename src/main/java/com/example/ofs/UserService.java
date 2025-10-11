package com.example.ofs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
	@Autowired
    private UserRepository userRepository;

    public Long findIdByUsername(String username) {
    	return userRepository.findByUsername(username).get().getId();
    }
    
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if (!user.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        return user;
    }


    public User createUser(User user) {
        // Check for duplicate username
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        // Check for duplicate email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
    
    public User getUserByUsername(String name) {
    	return userRepository.findByUsername(name).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User addFileToUser(Long userId, Long fileId) {
        User user = getUserById(userId);
        if (!user.getFileIds().contains(fileId)) {
            user.getFileIds().add(fileId);
        }
        return userRepository.save(user);
    }
    
    public Optional<User> removeFile(Long Fileid) {
    	Optional<User> u=userRepository.findUserByFileId(Fileid);
    	if(u.isEmpty())
    		return u;
    	User b=u.get();
    	if(b.getFileIds().remove(Fileid)) userRepository.save(b);
    	
    	return u;
    	
    }

    public User addFilesToUser(Long userId, List<Long> fileIds) {
        User user = getUserById(userId);
        for (Long fileId : fileIds) {
            if (!user.getFileIds().contains(fileId)) {
                user.getFileIds().add(fileId);
            }
        }
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id);
    }
}
