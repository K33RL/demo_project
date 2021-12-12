package com.example.demo_restful.controller;

import com.example.demo_restful.dto.EventType;
import com.example.demo_restful.dto.ObjectType;
import com.example.demo_restful.model.Message;
import com.example.demo_restful.model.Views;
import com.example.demo_restful.repository.MessageRepository;
import com.example.demo_restful.util.WsSender;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;

@RestController
@RequestMapping("/message")
public class MessageController {
    private final MessageRepository messageRepository;
    private final BiConsumer<EventType, Message> wsSender;

    @Autowired
    public MessageController(MessageRepository messageRepository, WsSender wsSender) {
        this.messageRepository = messageRepository;
        this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.IdName.class);
    }

    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Message> list() {
        return messageRepository.findAll();
    }

    @GetMapping("{id}")
    public Message getOneMessage(@PathVariable("id") Message message) {
        return message;
    }


    @PostMapping
    public Message createMessage(@RequestBody Message message) {
        message.setCreationDate(LocalDateTime.now());
        Message updatedMessage = messageRepository.save(message);

        wsSender.accept(EventType.CREATE, updatedMessage);

        return updatedMessage;
    }

    @PutMapping("{id}")
    public Message updateMessage(@PathVariable("id") Message messageFromDb, @RequestBody Message message) {
        BeanUtils.copyProperties(message, messageFromDb, "id");
        Message updatedMessage = messageRepository.save(messageFromDb);

        wsSender.accept(EventType.UPDATE, updatedMessage);

        return updatedMessage;

    }

    @DeleteMapping("{id}")
    public void deleteMessage(@PathVariable("id") Message message) {
        messageRepository.delete(message);
        wsSender.accept(EventType.REMOVE, message);
    }
}
