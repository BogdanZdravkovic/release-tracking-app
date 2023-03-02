package com.bogdan.releasetracking.service;

import com.bogdan.releasetracking.dto.ReleaseRequestWsDTO;
import com.bogdan.releasetracking.model.Release;

import java.util.List;

public interface ReleaseService {

    List<Release> getAllReleases();

    Release getReleaseById(Long id);

    Release updateRelease(ReleaseRequestWsDTO releaseWsDTO, Long id);

    Release createRelease(ReleaseRequestWsDTO releaseWsDTO);

    void deleteRelease(Long id);
}
