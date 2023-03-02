package com.bogdan.releasetracking.service.impl;

import com.bogdan.releasetracking.dto.UpdateReleaseWsDTO;
import com.bogdan.releasetracking.exception.ReleaseValidationException;
import com.bogdan.releasetracking.model.Release;
import com.bogdan.releasetracking.model.ReleaseStatus;
import com.bogdan.releasetracking.repository.ReleaseRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultReleaseServiceImplTest {

    @InjectMocks
    @Spy
    private DefaultReleaseServiceImpl releaseService;

    @Mock
    private ReleaseRepository releaseRepository;

    private Release release1;
    private Release release2;
    private UpdateReleaseWsDTO releaseWsDTO;

    @Before
    public void setUp() {
        release1 = new Release();
        release1.setId(1L);
        release1.setName("Release 1");
        release1.setDescription("Description 1");
        release1.setReleaseDate(LocalDate.parse("2022-12-31"));
        release1.setStatus(ReleaseStatus.CREATED);

        release2 = new Release();
        release2.setId(2L);
        release2.setName("Release 2");
        release2.setDescription("Description 2");
        release2.setReleaseDate(LocalDate.parse("2022-12-31"));
        release2.setStatus(ReleaseStatus.CREATED);

        releaseWsDTO = new UpdateReleaseWsDTO();
        releaseWsDTO.setName("New Release");
        releaseWsDTO.setDescription("New Release Description");
        releaseWsDTO.setReleaseDate("2022-12-31");
        releaseWsDTO.setStatus("CREATED");
    }

    @Test
    public void getAllReleases_returnsAllReleases() {
        List<Release> releaseList = new ArrayList<>();
        releaseList.add(release1);
        releaseList.add(release2);

        when(releaseRepository.findAll()).thenReturn(releaseList);

        List<Release> result = releaseService.getAllReleases();

        assertEquals(2, result.size());
        assertEquals(release1, result.get(0));
        assertEquals(release2, result.get(1));
    }

    @Test
    public void getReleaseById_returnsReleaseWithGivenId() {
        when(releaseRepository.findById(1L)).thenReturn(Optional.of(release1));

        Release result = releaseService.getReleaseById(1L);

        assertEquals(release1, result);
    }

    @Test(expected = ReleaseValidationException.class)
    public void getReleaseById_throwsExceptionIfNoReleaseWithGivenId() {
        when(releaseRepository.findById(1L)).thenReturn(Optional.empty());

        releaseService.getReleaseById(1L);
    }

    @Test
    public void updateRelease_updatesReleaseWithGivenId() {
        when(releaseRepository.findById(1L)).thenReturn(Optional.of(release1));

        Release result = releaseService.updateRelease(releaseWsDTO, 1L);

        assertEquals("New Release", result.getName());
        assertEquals("New Release Description", result.getDescription());
        assertEquals(LocalDate.parse("2022-12-31"), result.getReleaseDate());
        assertEquals(ReleaseStatus.CREATED, result.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void updateRelease_throwsExceptionIfNoReleaseWithGivenId() {
        when(releaseRepository.findById(1L)).thenReturn(Optional.empty());

        releaseService.updateRelease(releaseWsDTO, 1L);
    }

    @Test
    public void testCreateRelease() {
        UpdateReleaseWsDTO releaseWsDTO = new UpdateReleaseWsDTO();
        releaseWsDTO.setName("Test Release");
        releaseWsDTO.setDescription("Test Description");
        releaseWsDTO.setReleaseDate("2023-03-02");

        Release createdRelease = new Release();
        createdRelease.setId(1L);
        createdRelease.setName(releaseWsDTO.getName());
        createdRelease.setDescription(releaseWsDTO.getDescription());
        createdRelease.setReleaseDate(LocalDate.parse(releaseWsDTO.getReleaseDate()));
        createdRelease.setStatus(ReleaseStatus.CREATED);
        createdRelease.setCreatedAt(LocalDateTime.now());
        createdRelease.setLastUpdateAt(LocalDateTime.now());

        when(releaseRepository.save(any(Release.class))).thenReturn(createdRelease);

        Release resultRelease = releaseService.createRelease(releaseWsDTO);

        assertNotNull(resultRelease);
        assertEquals(resultRelease.getId(), createdRelease.getId());
        assertEquals(resultRelease.getName(), createdRelease.getName());
        assertEquals(resultRelease.getDescription(), createdRelease.getDescription());
        assertEquals(resultRelease.getReleaseDate(), createdRelease.getReleaseDate());
        assertEquals(resultRelease.getStatus(), createdRelease.getStatus());
        assertEquals(resultRelease.getCreatedAt(), createdRelease.getCreatedAt());
        assertEquals(resultRelease.getLastUpdateAt(), createdRelease.getLastUpdateAt());
    }

    @Test(expected = ReleaseValidationException.class)
    public void testCreateReleaseWithEmptyReleaseDate() {
        UpdateReleaseWsDTO releaseWsDTO = new UpdateReleaseWsDTO();
        releaseWsDTO.setName("Test Release");
        releaseWsDTO.setDescription("Test Description");
        releaseWsDTO.setReleaseDate("");

        releaseService.createRelease(releaseWsDTO);
    }

    @Test
    public void testDeleteRelease() {
        Long releaseId = 1L;

        doNothing().when(releaseRepository).deleteById(eq(releaseId));

        releaseService.deleteRelease(releaseId);

        verify(releaseRepository, times(1)).deleteById(eq(releaseId));
    }
}