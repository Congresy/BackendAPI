package com.conferencias.tfg.controller;

import com.conferencias.tfg.domain.Actor;
import com.conferencias.tfg.domain.Folder;
import com.conferencias.tfg.domain.Message;
import com.conferencias.tfg.repository.ActorRepository;
import com.conferencias.tfg.repository.FolderRepository;
import com.conferencias.tfg.repository.MessageRepository;
import com.conferencias.tfg.utilities.Views;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@CrossOrigin
@RestController
@RequestMapping("messages")
@Api(value="congresy", description="Operations pertaining to messages in Congresy")
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

    @ApiOperation(value = "Search a message in a certain folder by keyword", response = Iterable.class)
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

    @ApiOperation(value = "List all messages of a certain folder of an actor", response = Iterable.class)
    @GetMapping("/{idFolder}")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> getAllOfFolder(@PathVariable("idFolder") String idFolder) {
        Folder folder = folderRepository.findOne(idFolder);
        List<String> aux;
        List<Message> messages = new ArrayList<>();

        try {
            aux = folder.getMessages();

            for (String s : aux){
                messages.add(messageRepository.findOne(s));
            }

        } catch (NullPointerException e){
            messages = new ArrayList<>();
        }

        Comparator<Message> comparator = Comparator.comparing(t -> parseDate(t.getSentMoment()));

        messages.sort(comparator);

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @ApiOperation(value = "Get a certain message", response = Message.class)
    @GetMapping(value = "/{idMessage}")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> get(@PathVariable("idMessage") String id) {
        Message message = messageRepository.findOne(id);

        if (message == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(value = "Create a new message")
    @PostMapping(value = "/{idSender}/{idReceiver}", produces = "application/json")
    public ResponseEntity<?> create(@RequestBody Message message, @PathVariable("idSender") String idSender, @PathVariable("idReceiver") String idReceiver, UriComponentsBuilder ucBuilder) {

        Actor sender = actorRepository.findOne(idSender);
        Actor receiver = actorRepository.findOne(idReceiver);

        message.setSenderId(idSender);
        message.setReceiverId(idReceiver);
        messageRepository.save(message);

        List<String> senderFolders = sender.getFolders();
        List<String> receiverFolders = receiver.getFolders();

        Folder inbox = null;
        Folder outbox = null;

        for(String s : senderFolders)
            if(folderRepository.findOne(s).getName().equals("Outbox"))
                outbox = folderRepository.findOne(s);

        for(String s : receiverFolders)
            if(folderRepository.findOne(s).getName().equals("Inbox"))
                inbox = folderRepository.findOne(s);

        List<String> inboxMessages;
        List<String> outboxMessages;

        try {
            inboxMessages = inbox.getMessages();
            inboxMessages.add(message.getId());
            inbox.setMessages(inboxMessages);
            folderRepository.save(inbox);
        } catch (NullPointerException e){
            inboxMessages = new ArrayList<>();
            inboxMessages.add(message.getId());
            inbox.setMessages(inboxMessages);
            folderRepository.save(inbox);
        }

        try {
            outboxMessages = outbox.getMessages();
            outboxMessages.add(message.getId());
            outbox.setMessages(outboxMessages);
            folderRepository.save(outbox);

        } catch (NullPointerException e){
            outboxMessages = new ArrayList<>();
            outboxMessages.add(message.getId());
            outbox.setMessages(outboxMessages);
            folderRepository.save(outbox);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/message/{id}").buildAndExpand(message.getId()).toUri());

        return new ResponseEntity<>(message, headers, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Create a new message")
    @PostMapping(value = "/reply/{idMessage}/{idActor}", produces = "application/json")
    public ResponseEntity<?> reply(@RequestBody Message message, @PathVariable("idMessage") String idMessage, @PathVariable("idActor") String idActor) {

        Message message1 = messageRepository.findOne(idMessage);
        Actor receiver = actorRepository.findOne(message1.getReceiverId());
        Actor sender = actorRepository.findOne(message1.getSenderId());
        Actor actor = actorRepository.findOne(idActor);

        Actor fSender = null;
        Actor fReceiver = null;

        if (actor.equals(sender)){
            fSender = sender;
            fReceiver = receiver;
        } else if (actor.equals(receiver)){
            fSender = receiver;
            fReceiver = sender;
        }

        message.setSenderId(fSender.getId());
        message.setReceiverId(fReceiver.getId());
        messageRepository.save(message);

        // Set new body (reply)
        String body = message.getBody();
        body = body + "\n\n" + "--------- Original message ---------" + "\n\n" + message1.getBody();
        message.setBody(body);
        messageRepository.save(message);

        List<String> senderFolders = fSender.getFolders();
        List<String> receiverFolders = fReceiver.getFolders();

        Folder inbox = null;
        Folder outbox = null;

        for(String s : senderFolders)
            if(folderRepository.findOne(s).getName().equals("Outbox"))
                outbox = folderRepository.findOne(s);

        for(String s : receiverFolders)
            if(folderRepository.findOne(s).getName().equals("Inbox"))
                inbox = folderRepository.findOne(s);

        List<String> inboxMessages;
        List<String> outboxMessages;

        try {
            inboxMessages = inbox.getMessages();
            inboxMessages.add(message.getId());
            inbox.setMessages(inboxMessages);
            folderRepository.save(inbox);
        } catch (NullPointerException e){
            inboxMessages = new ArrayList<>();
            inboxMessages.add(message.getId());
            inbox.setMessages(inboxMessages);
            folderRepository.save(inbox);
        }

        try {
            outboxMessages = outbox.getMessages();
            outboxMessages.add(message.getId());
            outbox.setMessages(outboxMessages);
            folderRepository.save(outbox);

        } catch (NullPointerException e){
            outboxMessages = new ArrayList<>();
            outboxMessages.add(message.getId());
            outbox.setMessages(outboxMessages);
            folderRepository.save(outbox);
        }

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Send message to bin folder of a certain actor")
    @DeleteMapping(value = "/trash/{idActor}/{idMessage}", produces = "application/json")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> toTrash(@PathVariable("idActor") String idActor, @PathVariable("idMessage") String id) {
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
            if(folderRepository.findOne(s).getName().equals("Trash"))
                bin = folderRepository.findOne(s);
            if(folderRepository.findOne(s).getName().equals("Inbox"))
                inbox = folderRepository.findOne(s);
            if(folderRepository.findOne(s).getName().equals("Outbox"))
                outbox = folderRepository.findOne(s);
        }

        if (inbox.getMessages() != null) {
            if (inbox.getMessages().contains(message.getId())) {
                List<String> inboxMessages = inbox.getMessages();
                inboxMessages.remove(message.getId());
                inbox.setMessages(inboxMessages);
                folderRepository.save(inbox);
            }
        }

        if (outbox.getMessages() != null) {
            if(outbox.getMessages().contains(message.getId())){
                List<String> outboxMessages = outbox.getMessages();
                outboxMessages.remove(message.getId());
                inbox.setMessages(outboxMessages);
                folderRepository.save(outbox);
            }
        }

        try {
            List<String> binMessages = bin.getMessages();
            binMessages.add(message.getId());
            bin.setMessages(binMessages);
            folderRepository.save(bin);
        } catch (NullPointerException e){
            List<String> binMessages = new ArrayList<>();
            binMessages.add(message.getId());
            bin.setMessages(binMessages);
            folderRepository.save(bin);
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a certain message of an actor")
    @DeleteMapping(value = "/{idActor}/{idMessage}", produces = "application/json")
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
            if(folderRepository.findOne(s).getName().equals("Trash"))
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
