package com.bogdan.releasetracking.controller;

import com.bogdan.releasetracking.dto.UpdateReleaseWsDTO;
import com.bogdan.releasetracking.model.Release;
import com.bogdan.releasetracking.service.ReleaseService;
import com.bogdan.releasetracking.service.impl.ReleaseEventProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ReleaseTrackingControllerTest {

    private static final String TOPIC_NAME= "release_tracking";

    @InjectMocks
    @Spy
    private ReleaseTrackingController controller;

    @Mock
    private ReleaseService releaseService;

    @Mock
    private ReleaseEventProducer releaseEventProducer;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetAllReleases() {
        List<Release> releases = new ArrayList<>();
        releases.add(constructReleseObject());
        releases.add(constructReleseObject());

        when(releaseService.getAllReleases()).thenReturn(releases);

        List<Release> result = controller.getAllReleases();

        assertEquals(releases.size(), result.size());
        assertEquals(releases.get(0).getId(), result.get(0).getId());
        assertEquals(releases.get(0).getName(), result.get(0).getName());
        assertEquals(releases.get(1).getId(), result.get(1).getId());
        assertEquals(releases.get(1).getName(), result.get(1).getName());

        verify(releaseService, times(1)).getAllReleases();
        verifyNoMoreInteractions(releaseService);
    }

    @Test
    public void testGetReleaseById() {
        Long releaseId = 1L;
        Release release = constructReleseObject();

        when(releaseService.getReleaseById(releaseId)).thenReturn(release);

        ResponseEntity<Release> response = controller.getReleaseById(releaseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(release.getId(), response.getBody().getId());
        assertEquals(release.getName(), response.getBody().getName());

        verify(releaseService, times(1)).getReleaseById(releaseId);
        verifyNoMoreInteractions(releaseService);
    }

    @Test
    public void testGetReleaseByIdNotFound() {
        Long releaseId = 1L;

        when(releaseService.getReleaseById(releaseId)).thenReturn(null);

        ResponseEntity<Release> response = controller.getReleaseById(releaseId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(releaseService, times(1)).getReleaseById(releaseId);
        verifyNoMoreInteractions(releaseService);
    }

    @Test
    public void testCreateRelease() throws Exception {
        // Setup
        UpdateReleaseWsDTO releaseWsDTO = new UpdateReleaseWsDTO();
        releaseWsDTO.setName("Test Release");
        releaseWsDTO.setDescription("Test Description");

        Release newRelease = new Release();
        newRelease.setId(1L);
        newRelease.setName(releaseWsDTO.getName());
        newRelease.setDescription(releaseWsDTO.getDescription());

        when(releaseService.createRelease(any(UpdateReleaseWsDTO.class))).thenReturn(newRelease);

        // Exercise and Verify
        MvcResult result = mockMvc.perform(post("/releases")
                .content(asJsonString(releaseWsDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // Assert
        String location = result.getResponse().getHeader("Location");
        assertEquals("/releases/1", location);
    }

    @Test
    public void testUpdateRelease() throws Exception {
        // Setup
        Long id = 1L;
        UpdateReleaseWsDTO releaseWsDTO = new UpdateReleaseWsDTO();
        releaseWsDTO.setName("Test Release");
        releaseWsDTO.setDescription("Test Description");
        releaseWsDTO.setStatus("DONE");
        releaseWsDTO.setReleaseDate("2021-11-05");

        Release updatedRelease = new Release();
        updatedRelease.setId(id);
        updatedRelease.setName(releaseWsDTO.getName());
        updatedRelease.setDescription(releaseWsDTO.getDescription());

        when(releaseService.updateRelease(eq(releaseWsDTO), eq(id))).thenReturn(updatedRelease);

        ResponseEntity<Release> result = controller.updateRelease(id, releaseWsDTO);
        // Assert
        verify(releaseEventProducer).sendReleaseEvent(eq(TOPIC_NAME), any(String.class));
        Release responseRelease = result.getBody();
        assertEquals(updatedRelease.getId(), responseRelease.getId());
        assertEquals(updatedRelease.getName(), responseRelease.getName());
        assertEquals(updatedRelease.getDescription(), responseRelease.getDescription());
    }

    @Test
    public void testDeleteRelease() throws Exception {
        // Setup
        Long id = 1L;

        // Exercise and Verify
        mockMvc.perform(delete("/releases/{id}", id))
                .andExpect(status().isNoContent())
                .andReturn();

        // Assert
        verify(releaseService).deleteRelease(eq(id));
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Release constructReleseObject(){
        Release release = new Release();
        release.setName("release1");
        release.setName("release description");
        release.setReleaseDate(LocalDate.now());
        return release;
    }
}