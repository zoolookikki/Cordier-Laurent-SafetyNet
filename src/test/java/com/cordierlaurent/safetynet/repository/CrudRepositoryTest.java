package com.cordierlaurent.safetynet.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public abstract class CrudRepositoryTest<Model> {

    // attention, pensez aux assertions de contrôle de l'initialisation ci-dessous.
    protected CrudRepository<Model> repository;
    protected Model model1;
    protected Model model1Updated;
    protected String[] id1UpdatedExist; //  new String[]{"John", "Boyd"}
    protected String[] id1UpdatedNotExist; //  new String[]{"xxxxx", "Boyd"}
    protected Model model2;
    protected String[] id2; // new String[]{"Jacob", "Boyd"}
    protected Model model3;
    
    // à implémenter par les classes enfants.

    // cette fonction doit initialiser toutes les variables ci-dessus qui sont protected, dont la fille va hériter.
    protected abstract void init();
    // cette fonction initialise le jeu d'essai.
    protected abstract void initModels();
    
    // ne pas faire de @BeforeAll => static, quand héritage. Pas de problème de performance ici, je peux passre en @BeforeEach, voir pour le futur si il y a une solution.
    // @BeforeAll
    @BeforeEach
    private void setUpPerTest() {

        init() ;
        
        // pour vérifier que la fille n'a rien oublié d'initialiser avant de commencer.
        assertNotNull(repository, "repository uninitialized");        
        assertNotNull(model1, "model1 uninitialized");
        assertNotNull(model1Updated, "model1Updated uninitialized");
        assertNotNull(id1UpdatedExist, "id1UpdatedExist uninitialized");
        assertNotNull(id1UpdatedNotExist, "id1UpdatedNotExist uninitialized");
        assertNotNull(model2, "model2 uninitialized");
        assertNotNull(id2, "id2 uninitialized");
        assertNotNull(model3, "model3 uninitialized");
        
        repository.setModels(new ArrayList<>());
    }
    
    @AfterAll
    private static void tearDown(){
    }
    
    @Test
//    @Disabled
    @DisplayName("Add models and verifies that the returned list is up to date")
    void getPersonsTest() {

        // given
        repository.addModel(model1);
        repository.addModel(model2);
        
        // when
        List<Model> models = repository.getModels();
        
        // then
        assertThat(models)
            .isNotNull()
            .hasSize(2)
            // je préfère utiliser containsExactlyInAnyOrder à containsExactly car si je change de type de list, le test va échoué et l'ordre n'a pas d'importance ici.
            // le test est suffisant car equals qui est généré par @Data.
            .containsExactlyInAnyOrder(model1, model2); 
    }

    @Test
    @DisplayName("Replaces the existing list with a new list")
    void setPersonsTest() {
         
         // given
         repository.addModel(model1);
         repository.addModel(model2);

         //when
         List<Model> modelsNewList = new ArrayList<Model>();
         modelsNewList.add(model3);
         repository.setModels(modelsNewList);

         //then
         List<Model> models = repository.getModels();
         assertThat(models)
             .isNotNull()
             .hasSize(1)
             .containsExactly(model3); 
        
    }

    @Test
    @DisplayName("A model has been added")
    void addtPersonsTest() {

        // given : repository empty.

        // when
        repository.addModel(model2);
        
        //then
        List<Model> models = repository.getModels();
        assertThat(models)
            .isNotNull()
            .hasSize(1)
            .containsExactly(model2);

    }
    
    @Test
    @DisplayName("Update of a model who exists with the unique key")
    void updatePersonsTest() {
        
        // given
        repository.addModel(model1);
        
        // when
        boolean result = repository.updateModelByUniqueKey(id1UpdatedExist, model1Updated);
        List<Model> models = repository.getModels();
        
        // then
        assertThat(result).isTrue();
        assertThat(models)
            .isNotNull()
            .hasSize(1)
            .containsExactly(model1Updated);
        
    }
        
    @Test
    @DisplayName("Update of a model who does not exist with the unique key")
    void updatePersonsFailTest() {
        
        // given
        repository.addModel(model1);
        repository.addModel(model2);
        repository.addModel(model3);
        
        // when
        boolean result = repository.updateModelByUniqueKey (id1UpdatedNotExist, model1Updated);
                
        // then
        assertThat(result).isFalse();
       
    }
    
    @Test
    @DisplayName("Delete an existing model by the unique key")
    void deletePersonsTest() {
        
        // given
        repository.addModel(model1);
        repository.addModel(model2);
        repository.addModel(model3);
        
        // when
        boolean result = repository.deleteModelByUniqueKey(id2);
        List<Model> models = repository.getModels();
                
        // then
        assertThat(result).isTrue();
        assertThat(models)
            .isNotNull()
            .hasSize(2)
            .containsExactlyInAnyOrder(model1, model3); 
       
    }
    
    @Test
    @DisplayName("Delete a model who doesn't exist by the unique key")
    void deletePersonsFailTest() {
        
        // given
        repository.addModel(model1);
        repository.addModel(model2);
        repository.addModel(model3);
        
        // when
        boolean result = repository.deleteModelByUniqueKey(id1UpdatedNotExist); 
                
        // then
        assertThat(result).isFalse();
       
    }
    
}
