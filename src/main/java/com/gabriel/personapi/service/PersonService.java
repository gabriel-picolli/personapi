package com.gabriel.personapi.service;


import com.gabriel.personapi.dto.request.PersonDTO;
import com.gabriel.personapi.dto.response.MessageResponseDTO;
import com.gabriel.personapi.entity.Person;
import com.gabriel.personapi.exception.PersonNotFoundException;
import com.gabriel.personapi.mapper.PersonMapper;
import com.gabriel.personapi.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private PersonRepository personRepository;

    private final PersonMapper personMapper = PersonMapper.INSTANCE;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public MessageResponseDTO createPerson(PersonDTO personDTO) {

        Person personToSave = personMapper.toModel(personDTO);

        Person savedPerson = personRepository.save(personToSave);
        return MessageResponseDTO
                .builder()
                .message("Created person with ID" + savedPerson.getId())
                .build();
    }

    public List<PersonDTO> listAll() {
        List<Person> allpeople = personRepository.findAll();
        return allpeople.stream()
                .map(personMapper::toDTO)
                .collect(Collectors.toList());

    }

    public PersonDTO findById(Long id) throws PersonNotFoundException {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));

        return personMapper.toDTO(person);


    }

    public void delete(Long id) throws PersonNotFoundException {
        personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));

        personRepository.deleteById(id);

    }
}
