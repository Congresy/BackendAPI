package com.conferencias.tfg.controller;

import com.conferencias.tfg.domain.Announcement;
import com.conferencias.tfg.domain.Conference;
import com.conferencias.tfg.repository.AnnouncementRepository;
import com.conferencias.tfg.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @GetMapping("/all")
    public List<Announcement> showAll() {
        return announcementRepository.findAll();
    }

    @PostMapping("/create")
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

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> edit(@PathVariable("id") String id, @RequestBody Announcement announcement) {
        Announcement currentAnnouncement = announcementRepository.findOne(id);

        if (currentAnnouncement == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }
        announcementService.edit(currentAnnouncement, announcement);
        return new ResponseEntity<>(currentAnnouncement, HttpStatus.OK);
    }


    @DeleteMapping(value = "/delete/{id}")
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
