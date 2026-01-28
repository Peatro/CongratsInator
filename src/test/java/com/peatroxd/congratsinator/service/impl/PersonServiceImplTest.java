package com.peatroxd.congratsinator.service.impl;

import com.peatroxd.congratsinator.enums.NotFoundExceptionMessage;
import com.peatroxd.congratsinator.model.Person;
import com.peatroxd.congratsinator.repository.PersonRepository;
import com.peatroxd.congratsinator.testdata.Paths;
import com.peatroxd.congratsinator.testdata.People;
import com.peatroxd.congratsinator.testdata.Persons;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonServiceImpl personServiceImpl;

    @Test
    void addPerson_savesAndReturnsSavedEntity() {
        Person input = Persons.toSave();
        Person saved = Persons.persisted();

        when(personRepository.save(input)).thenReturn(saved);

        Person result = personServiceImpl.addPerson(input);

        assertThat(result).isSameAs(saved);
        verify(personRepository).save(input);
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void deletePerson_whenExists_deletesIt() {
        UUID id = UUID.randomUUID();
        Person existing = Persons.persisted(id);

        when(personRepository.findById(id)).thenReturn(Optional.of(existing));

        personServiceImpl.deletePerson(id);

        verify(personRepository).findById(id);
        verify(personRepository).delete(existing);
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void deletePerson_whenNotExists_doesNothing() {
        UUID id = UUID.randomUUID();
        when(personRepository.findById(id)).thenReturn(Optional.empty());

        personServiceImpl.deletePerson(id);

        verify(personRepository).findById(id);
        verify(personRepository, never()).delete(any());
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void editPerson_savesAndReturnsSavedEntity() {
        Person input = Persons.withoutPhotoKey();
        Person saved = Persons.persisted();

        when(personRepository.save(input)).thenReturn(saved);

        Person result = personServiceImpl.editPerson(input);

        assertThat(result).isSameAs(saved);
        verify(personRepository).save(input);
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void getAll_returnsRepositoryResult() {
        List<Person> persons = People.list(3);

        when(personRepository.findAll()).thenReturn(persons);

        List<Person> result = personServiceImpl.getAll();

        assertThat(result).isSameAs(persons);
        verify(personRepository).findAll();
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void getById_whenExists_returnsEntity() {
        UUID id = UUID.randomUUID();
        Person existing = Persons.persisted(id);

        when(personRepository.findById(id)).thenReturn(Optional.of(existing));

        Person result = personServiceImpl.getById(id);

        assertThat(result).isSameAs(existing);
        verify(personRepository).findById(id);
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void getById_whenNotExists_throwsEntityNotFound() {
        UUID id = UUID.randomUUID();
        when(personRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> personServiceImpl.getById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(NotFoundExceptionMessage.PERSON_NOT_FOUND.getMessage() + id);

        verify(personRepository).findById(id);
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void updatePhotoPath_whenExists_updatesPhotoKey() {
        UUID id = UUID.randomUUID();
        Person existing = Persons.persisted(id);
        String newPhotoPath = Paths.photoFor(id);

        when(personRepository.findById(id)).thenReturn(Optional.of(existing));

        personServiceImpl.updatePhotoPath(id, newPhotoPath);

        assertThat(existing.getPhotoKey()).isEqualTo(newPhotoPath);
        verify(personRepository).findById(id);

        verify(personRepository, never()).save(any());
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    void updatePhotoPath_whenNotExists_throwsEntityNotFound() {
        UUID id = UUID.randomUUID();
        when(personRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> personServiceImpl.updatePhotoPath(id, "persons/x.png"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(NotFoundExceptionMessage.PERSON_NOT_FOUND.getMessage() + id);

        verify(personRepository).findById(id);
        verify(personRepository, never()).save(any());
        verifyNoMoreInteractions(personRepository);
    }
}
