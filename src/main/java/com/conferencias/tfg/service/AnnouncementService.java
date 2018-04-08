package com.conferencias.tfg.service;

import com.conferencias.tfg.domain.Announcement;
import com.conferencias.tfg.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    public Announcement edit(Announcement actual, Announcement editado) {
        actual.setPicture(editado.getPicture());
        actual.setUrl(editado.getUrl());
        return announcementRepository.save(actual);

    }
}
