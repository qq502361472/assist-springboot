package com.hjrpc.assistspringboot.controller;

import com.hjrpc.assistspringboot.annoation.DictValue;
import com.hjrpc.assistspringboot.dto.PersonDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    @GetMapping("get")
    @DictValue
    public PersonDTO get(){
        PersonDTO personDTO3 = new PersonDTO();
        personDTO3.setStatus(2);
        personDTO3.setAge(2);

        List<PersonDTO> result = new ArrayList<>();
        PersonDTO personDTO = new PersonDTO();
        personDTO.setStatus(0);
        personDTO.setAge(0);
        result.add(personDTO);

        PersonDTO personDTO1 = new PersonDTO();
        personDTO1.setStatus(1);
        personDTO1.setAge(1);
        result.add(personDTO1);

        personDTO3.setChild(result);

        return personDTO3;
    }
}
