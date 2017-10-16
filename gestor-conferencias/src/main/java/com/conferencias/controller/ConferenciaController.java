package com.conferencias.controller;

import com.conferencias.model.Conferencia;
import com.conferencias.repository.ConferenciaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conferencias")
public class ConferenciaController {

    private ConferenciaRepository conferenciaRepository;

    public ConferenciaController(ConferenciaRepository conferenciaRepository){
        this.conferenciaRepository = conferenciaRepository;
    }


    @GetMapping("/all")
    public List<Conferencia> getAll(){
        List<Conferencia> conferencias = conferenciaRepository.findAll();

        return conferencias;
    }

    @PutMapping
    public void createConference(@RequestBody Conferencia conferencia){
        this.conferenciaRepository.insert(conferencia);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id){
        this.conferenciaRepository.delete(this.conferenciaRepository.findOne(id));
    }
}
