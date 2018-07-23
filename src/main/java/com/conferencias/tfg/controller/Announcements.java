package com.conferencias.tfg.controller;

import com.conferencias.tfg.domain.Announcement;
import com.conferencias.tfg.domain.Conference;
import com.conferencias.tfg.repository.AnnouncementRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("announcements")
@Api(value = "Announcements", description = "Operations related with announcements")
public class Announcements {

    private final AnnouncementRepository announcementRepository;

    @Autowired
    public Announcements(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    @ApiOperation(value = "List all system's announcements", response = Iterable.class)
    @GetMapping()
    public List<Announcement> showAll() {
        return announcementRepository.findAll();
    }

    @ApiOperation(value = "Get an anonuncement", response = Announcement.class)
    @GetMapping("/{idAnnouncement}")
    public ResponseEntity<?> showAll(@PathVariable("idAnnouncement") String idAnnouncement) {

        return new ResponseEntity<>(announcementRepository.findOne(idAnnouncement), HttpStatus.OK);
    }

    @ApiOperation(value = "Create an announcement")
    @PostMapping(produces = "application/json")
    public ResponseEntity<?> create(@RequestBody Announcement announcement) {

        if (this.actorExist(announcement)) {
            return new ResponseEntity<Error>(HttpStatus.CONFLICT);
        }

        announcementRepository.save(announcement);

        return new ResponseEntity<>(announcement, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Delete an announcement by ID")
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        Announcement announcement = announcementRepository.findOne(id);
        if (announcement == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }
        announcementRepository.delete(id);
        return new ResponseEntity<Conference>(HttpStatus.NO_CONTENT);
    }

    private Boolean actorExist(Announcement announcement) {
        Boolean res = false;

        for (Announcement a : announcementRepository.findAll()) {
            if (announcement.equals(a)) {
                res = true;
                break;
            }
        }
        return res;
    }
}
