package com.conferencias.tfg.controller;

import com.conferencias.tfg.domain.Announcement;
import com.conferencias.tfg.domain.Conference;
import com.conferencias.tfg.repository.AnnouncementRepository;
import com.conferencias.tfg.service.AnnouncementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("announcements")
@Api(value = "Announcements", description = "Operations related with announcements")
public class Announcements {

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @ApiOperation(value = "List all system's announcements", response = Iterable.class)
    @GetMapping()
    public List<Announcement> showAll() {
        return announcementRepository.findAll();
    }

    @ApiOperation(value = "Create an announcement")
    @PostMapping(produces = "application/json")
    public ResponseEntity<?> create(@RequestBody Announcement announcement, UriComponentsBuilder ucBuilder) {

        if (this.actorExist(announcement)) {
            return new ResponseEntity<Error>(HttpStatus.CONFLICT);
        }
        Announcement aux = new Announcement();
        announcement.setId(aux.getId());
        announcementRepository.save(announcement);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/announcement/{id}").buildAndExpand(announcement.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update an announcement by ID")
    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> edit(@PathVariable("id") String id, @RequestBody Announcement announcement) {
        Announcement currentAnnouncement = announcementRepository.findOne(id);

        if (currentAnnouncement == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }
        announcementService.edit(currentAnnouncement, announcement);
        return new ResponseEntity<>(currentAnnouncement, HttpStatus.OK);
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
