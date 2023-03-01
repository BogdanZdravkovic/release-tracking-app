package com.bogdan.releasetracking.service;

import com.bogdan.releasetracking.dto.UpdateReleaseWsDTO;
import com.bogdan.releasetracking.model.Release;

import java.util.List;

public interface ReleaseService {

    List<Release> getAllReleases();

    Release getReleaseById(Long id);

    Release updateRelease(UpdateReleaseWsDTO releaseWsDTO, Long id);

    Release createRelease(UpdateReleaseWsDTO releaseWsDTO);

    void deleteRelease(Long id);
}
