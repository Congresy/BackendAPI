package com.conferencias.tfg.service;


import com.conferencias.tfg.domain.Actor;
import com.conferencias.tfg.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Transactional
@Service
public class ActorService{

    private final ActorRepository actorRepository;

    @Autowired
    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public Actor edit(Actor currentActor,Actor actor){
        currentActor.setName(actor.getName());
        currentActor.setSurname(actor.getSurname());
        currentActor.setEmail(actor.getEmail());
        currentActor.setPhone(actor.getPhone());
        currentActor.setPhoto(actor.getPhoto());
        currentActor.setPlace(actor.getPlace());
        Actor res = actorRepository.save(currentActor);
        return res;
    }

    public static String encryptPassword(String password) {
        String generatedPassword = "";
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(password.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;

    }
}
