package com.bogdan.releasetracking.service.impl;


import com.bogdan.releasetracking.dto.UpdateReleaseWsDTO;
import com.bogdan.releasetracking.exception.ReleaseValidationException;
import com.bogdan.releasetracking.model.Release;
import com.bogdan.releasetracking.model.ReleaseStatus;
import com.bogdan.releasetracking.repository.ReleaseRepository;
import com.bogdan.releasetracking.service.ReleaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class DefaultReleaseServiceImpl  implements ReleaseService {

    @Autowired
    private ReleaseRepository releaseRepository;

    public DefaultReleaseServiceImpl(ReleaseRepository releaseRepository) {
        this.releaseRepository = releaseRepository;
    }


    @Override
    public List<Release> getAllReleases() {
        return releaseRepository.findAll();
    }

    @Override
    public Release getReleaseById(Long id) {
        return releaseRepository.findById(id).orElseThrow(() -> new ReleaseValidationException("No release found for given ID.", String.valueOf(id)));
    }

    @Override
    public Release updateRelease(UpdateReleaseWsDTO releaseWsDTO, Long id) {
        Release currentRelease = releaseRepository.findById(id).orElseThrow(RuntimeException::new);
        currentRelease.setName(releaseWsDTO.getName());
        currentRelease.setDescription(releaseWsDTO.getDescription());
        currentRelease.setStatus(Enum.valueOf(ReleaseStatus.class, releaseWsDTO.getStatus()));
        currentRelease.setReleaseDate(releaseWsDTO.getReleaseDate());
        currentRelease.setLastUpdateAt(LocalDateTime.now());
        releaseRepository.save(currentRelease);
        return currentRelease;
    }

    @Override
    public Release createRelease(UpdateReleaseWsDTO releaseWsDTO) {
        Release newRelease = new Release();
        newRelease.setName(releaseWsDTO.getName());
        newRelease.setDescription(releaseWsDTO.getDescription());
        newRelease.setReleaseDate(releaseWsDTO.getReleaseDate());
        newRelease.setStatus(ReleaseStatus.CREATED);
        newRelease.setCreatedAt(LocalDateTime.now());
        newRelease.setLastUpdateAt(LocalDateTime.now());
        releaseRepository.save(newRelease);
        return newRelease;
    }

    @Override
    public void deleteRelease(Long id) {
        releaseRepository.deleteById(id);
    }
}
