package com.cordierlaurent.safetynet.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

// .... à revoir pourquoi cela ne s'importe pas automatiquement :
// ne s'est pas importé automatiquement avec Eclipse ==> méthode post(String) undefined 
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
// idem pas automatique : cet import regroupe toutes les méthodes comme : status() jsonPath() content() view() redirectedUrl()
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cordierlaurent.safetynet.repository.JsonDataRepository;
import com.cordierlaurent.safetynet.service.CrudService;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class CrudControllerTest<Model> {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

//  @MockitoBean  // idem @Mock + injecté dans Spring et remplace le bean réel (remplace @MockBean déprécié).
    @MockitoBean
    protected JsonDataRepository jsonDataRepository;

    // To do the test of each child class :
    // For ex : "/person".
    protected abstract String getBaseUrl(); 
    // Must be a correct Model for positive test.
    protected abstract Model getValidModel(); 
    // Id for update or deletion tests.
    protected abstract String[] getIdForUpdate();
    // This model must not be a duplicate.
    protected abstract Model getUpdatedModel();
    // The service must be mocked by the child class.
    protected abstract CrudService<Model> getMockService();     

    @Test
    void addModelTest() throws Exception {

        // given
        Model validModel = getValidModel();
        when(getMockService().isUnique(null, validModel)).thenReturn(true);

        // when then
        mockMvc.perform(post(getBaseUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validModel)))
            .andExpect(status().isCreated());

        verify(getMockService(), times(1)).isUnique(null, validModel);
        verify(getMockService(), times(1)).addModel(validModel);
        verify(jsonDataRepository, times(1)).save();
    }

    @Test
    void addModelFailTest() throws Exception {

        // given
        Model validModel = getValidModel();
        when(getMockService().isUnique(null, validModel)).thenReturn(false); 

        // when then
        mockMvc.perform(post(getBaseUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validModel)))
            .andExpect(status().isConflict());

        verify(getMockService(), times(1)).isUnique(null, validModel);
        verify(getMockService(), times(0)).addModel(validModel);
        verify(jsonDataRepository, times(0)).save();
    }

    @Test
    void updateModelByUniqueKeyTest() throws Exception {

        // given
        Model updatedModel = getUpdatedModel();
        String[] id = getIdForUpdate();
        when(getMockService().isUnique(id, updatedModel)).thenReturn(true);
        when(getMockService().updateModelByUniqueKey(id, updatedModel)).thenReturn(true);

        // when then
        mockMvc.perform(put(getBaseUrl() + "/{param1}/{param2}", id[0], id[1])
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedModel)))
            .andExpect(status().isOk());

        verify(getMockService(), times(1)).isUnique(id, updatedModel);
        verify(getMockService(), times(1)).updateModelByUniqueKey(id, updatedModel);
        verify(jsonDataRepository, times(1)).save();
    }

    @Test
    void updateModelByUniqueKeyConflictTest() throws Exception {

        // given
        Model updatedModel = getUpdatedModel();
        String[] id = getIdForUpdate();
        when(getMockService().isUnique(id, updatedModel)).thenReturn(false); 

        // when then
        mockMvc.perform(put(getBaseUrl() + "/{param1}/{param2}", id[0], id[1])
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedModel)))
            .andExpect(status().isConflict());

        verify(getMockService(), times(1)).isUnique(id, updatedModel);
        verify(getMockService(), times(0)).updateModelByUniqueKey(id, updatedModel); 
        verify(jsonDataRepository, times(0)).save(); 
    }

    @Test
    void updateModelByUniqueKeyNotFoundTest() throws Exception {

        // given
        Model updatedModel = getUpdatedModel();
        String[] id = getIdForUpdate();
        when(getMockService().isUnique(id, updatedModel)).thenReturn(true);
        when(getMockService().updateModelByUniqueKey(id, updatedModel)).thenReturn(false); 

        // when then
        mockMvc.perform(put(getBaseUrl() + "/{param1}/{param2}", id[0], id[1])
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedModel)))
            .andExpect(status().isNotFound());

        verify(getMockService(), times(1)).updateModelByUniqueKey(id, updatedModel);
        verify(jsonDataRepository, times(0)).save();
    }

    @Test
    void deleteModelByUniqueKeyTest() throws Exception {

        // given
        String[] id = getIdForUpdate();
        when(getMockService().deleteModelByUniqueKey(id)).thenReturn(true);

        // when then
        mockMvc.perform(delete(getBaseUrl() + "/{param1}/{param2}", id[0], id[1]))
            .andExpect(status().isNoContent());

        verify(getMockService(), times(1)).deleteModelByUniqueKey(id);
        verify(jsonDataRepository, times(1)).save();
    }
    
    @Test
    void deleteModelByUniqueKeyNotFoundTest() throws Exception {

        // given
        String[] id = getIdForUpdate();
        when(getMockService().deleteModelByUniqueKey(id)).thenReturn(false);

        // when then
        mockMvc.perform(delete(getBaseUrl() + "/{param1}/{param2}", id[0], id[1]))
            .andExpect(status().isNotFound());

        verify(getMockService(), times(1)).deleteModelByUniqueKey(id);
        verify(jsonDataRepository, times(0)).save();
    }
    
}
