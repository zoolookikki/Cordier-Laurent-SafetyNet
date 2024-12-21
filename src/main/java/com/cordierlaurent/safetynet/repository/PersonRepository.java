package com.cordierlaurent.safetynet.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import lombok.NonNull;

import com.cordierlaurent.safetynet.model.Person;

@Repository
public class PersonRepository {
    
    // Liste dans l'ordre car c'est une petite liste, je ne cherche pas l'optmisation en recherche.
    private List<Person> persons = new ArrayList<>();

    // pour récupérer toutes les personnes
    // retourne une copie : attention au passage par référence des objets.
    public List<Person> getPersons() {
        return new ArrayList<>(persons);  
    }

    // pour remplacer toute la liste de personnes
    // création d'une nouvelle liste : attention au passage par référence des objets.
    public void setPersons(List<Person> newPersons) {
        persons = new ArrayList<>(newPersons);  
    }

    // ajouter une personne
    // @NonNull gère l'exception si person est null.
    public void addPerson(@NonNull Person person) {
        persons.add(person);
    }

    // mise à jour avec la clef nom, prénom qui est l'identificateur unique.
    public boolean updatePersonByFirstNameAndLastName (String firstName, String lastName, @NonNull Person personToUpdate) {
        for (Person person : persons) {
            if (person.getFirstName().equalsIgnoreCase(firstName) &&
                person.getLastName().equalsIgnoreCase(lastName)) {
                persons.set(persons.indexOf(person), personToUpdate);
                return true;
            }
        }
        return false;
    }
    
    // Suppression idem par clef unique avec lambda.
    public boolean deletePersonByFirstNameAndLastName(String firstName, String lastName) {
        return persons.removeIf(person ->
            person.getFirstName().equalsIgnoreCase(firstName) &&
            person.getLastName().equalsIgnoreCase(lastName)
        );
    }
    
}
