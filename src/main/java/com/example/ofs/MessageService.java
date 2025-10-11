package com.example.ofs;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // Save a new message (default viewed=false)
    public Message saveMessage(Message message) {
        message.setViewed(false);
        return messageRepository.save(message);
    }

    // Get all messages
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // Get single message
    public Message getMessageById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
    }

    // Mark as viewed
    public Message markAsViewed(Long id) {
        Message message = getMessageById(id);
        message.setViewed(true);
        return messageRepository.save(message);
    }

    // Delete message
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }
}
