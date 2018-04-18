package com.conferencias.tfg.controller;

import com.conferencias.tfg.domain.Actor;
import com.conferencias.tfg.domain.Folder;
import com.conferencias.tfg.domain.Message;
import com.conferencias.tfg.repository.ActorRepository;
import com.conferencias.tfg.repository.FolderRepository;
import com.conferencias.tfg.repository.MessageRepository;
import com.conferencias.tfg.utilities.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("messages")
public class Messages {

    private MessageRepository messageRepository;
    private FolderRepository folderRepository;
    private ActorRepository actorRepository;

    @Autowired
    public Messages(MessageRepository messageRepository, FolderRepository folderRepository, ActorRepository actorRepository) {
        this.messageRepository = messageRepository;
        this.folderRepository = folderRepository;
        this.actorRepository = actorRepository;
    }

    @GetMapping("/search/{idActor}/{folderName}/{keyword}")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> searchInFolderOfActor(@PathVariable("folderName") String folder, @PathVariable("idActor") String idActor, @PathVariable("keyword") String keyword) {
        Actor actor = actorRepository.findOne(idActor);
        Folder folderToShow = null;
        List<Message> messages = new ArrayList<>();

        for(String s : actor.getFolders()) {
            if(folderRepository.findOne(s).getName().equals(folder))
                folderToShow = folderRepository.findOne(s);
        }

        for(String s : folderToShow.getMessages()){
            if(messageRepository.findOne(s).getBody().toLowerCase().contains(keyword.toLowerCase()) || messageRepository.findOne(s).getSubject().toLowerCase().contains(keyword.toLowerCase()))
                messages.add(messageRepository.findOne(s));
        }

        if(messages.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Comparator<Message> comparator = new Comparator<Message>() {
            @Override
            public int compare(Message t1, Message t2) {
                return parseDate(t1.getSentMoment()).compareTo(parseDate(t2.getSentMoment()));
            }
        };

        messages.sort(comparator);

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/all/{idActor}/{folderName}/")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> getAllOfFolderOfActor(@PathVariable("folderName") String folder, @PathVariable("idActor") String idActor) {
        Actor actor = actorRepository.findOne(idActor);
        Folder folderToShow = null;
        List<Message> messages = new ArrayList<>();

        for(String s : actor.getFolders()) {
            if(folderRepository.findOne(s).getName().equals(folder))
                folderToShow = folderRepository.findOne(s);
        }

        for(String s : folderToShow.getMessages()){
            messages.add(messageRepository.findOne(s));
        }

        Comparator<Message> comparator = new Comparator<Message>() {
            @Override
            public int compare(Message t1, Message t2) {
                return parseDate(t1.getSentMoment()).compareTo(parseDate(t2.getSentMoment()));
            }
        };

        messages.sort(comparator);

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping(value = "/{idMessage}")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> get(@PathVariable("idMessage") String id) {
        Message message = messageRepository.findOne(id);

        if (message == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/{idSender}/{idReceiver}")
    public ResponseEntity<?> create(@RequestBody Message message, @PathVariable("idSender") String idSender, @PathVariable("idReceiver") String idReceiver, UriComponentsBuilder ucBuilder) {

        Actor sender = actorRepository.findOne(idSender);
        Actor receiver = actorRepository.findOne(idReceiver);

        List<String> senderFolders = sender.getFolders();
        List<String> receiverFolders = receiver.getFolders();

        Folder inbox = null;
        Folder outbox = null;

        for(String s : senderFolders)
            if(folderRepository.findOne(s).getName().equals("Outbox"))
                inbox = folderRepository.findOne(s);

        for(String s : receiverFolders)
            if(folderRepository.findOne(s).getName().equals("Inbox"))
                inbox = folderRepository.findOne(s);

        List<String> inboxMessages = inbox.getMessages();
        List<String> outboxMessages = outbox.getMessages();

        inboxMessages.add(message.getId());
        outboxMessages.add(message.getId());

        inbox.setMessages(inboxMessages);
        outbox.setMessages(outboxMessages);

        folderRepository.save(inbox);
        folderRepository.save(outbox);

        messageRepository.save(message);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/message/{id}").buildAndExpand(message.getId()).toUri());

        return new ResponseEntity<>(message, headers, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/bin/{idActor}/idMessage")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> toBin(@PathVariable("idActor") String idActor, @PathVariable("idMessage") String id) {
        Message message = messageRepository.findOne(id);
        Actor actor = actorRepository.findOne(idActor);

        if (message == null || actor == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }

        List<String> folders = actor.getFolders();

        Folder bin = null;
        Folder inbox = null;
        Folder outbox = null;

        for(String s : folders){
            if(folderRepository.findOne(s).getName().equals("Bin"))
                bin = folderRepository.findOne(s);
            if(folderRepository.findOne(s).getName().equals("Inbox"))
                inbox = folderRepository.findOne(s);
            if(folderRepository.findOne(s).getName().equals("Outbox"))
                outbox = folderRepository.findOne(s);
        }

        if(inbox.getMessages().contains(message.getId())){
            List<String> inboxMessages = inbox.getMessages();
            inboxMessages.remove(message.getId());
            inbox.setMessages(inboxMessages);
            folderRepository.save(inbox);
        } else if(outbox.getMessages().contains(message.getId())){
            List<String> outboxMessages = outbox.getMessages();
            outboxMessages.remove(message.getId());
            inbox.setMessages(outboxMessages);
            folderRepository.save(outbox);
        }

        List<String> binMessages = bin.getMessages();
        binMessages.add(message.getId());

        bin.setMessages(binMessages);

        folderRepository.save(bin);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{idActor}/{idMessage}")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> delete(@PathVariable("idActor") String idActor, @PathVariable("idMessage") String id) {
        Message message = messageRepository.findOne(id);
        Actor actor = actorRepository.findOne(idActor);

        if (message == null || actor == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }

        List<String> folders = actor.getFolders();

        Folder bin = null;

        for(String s : folders)
            if(folderRepository.findOne(s).getName().equals("Bin"))
                bin = folderRepository.findOne(s);

        List<String> binMessages = bin.getMessages();
        binMessages.remove(message.getId());

        bin.setMessages(binMessages);

        folderRepository.save(bin);
        messageRepository.delete(message);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // ---------------------------------------------------------------------------------------------------------------//
	// ----------------------------------------------- Métodos auxiliares --------------------------------------------//
	// ---------------------------------------------------------------------------------------------------------------//

    private LocalDateTime parseDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        return dateTime;
    }
}
