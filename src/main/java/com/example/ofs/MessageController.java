package com.example.ofs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;
    @Autowired
    private MessageRepository mr;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // User sends a message
    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestParam String username,@RequestParam String name,@RequestParam String contact,@RequestParam String message,@RequestParam boolean viewed) {
    	Message s=new Message();
    	s.setUsername(username);
    	s.setName(name);
    	s.setContact(contact);
    	s.setMessage(message);
    	s.setViewed(false);
    	s=mr.save(s);
        return ResponseEntity.ok(s);
    }

    // Admin fetches all messages
    @GetMapping
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    // Admin fetches a specific message
    @GetMapping("/{id}")
    public Message getMessage(@PathVariable Long id) {
        return messageService.getMessageById(id);
    }

    // Admin marks a message as viewed
    @PutMapping("/{id}/view")
    public Message markAsViewed(@PathVariable Long id) {
        return messageService.markAsViewed(id);
    }

    // Admin deletes a message
    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
    }
}
