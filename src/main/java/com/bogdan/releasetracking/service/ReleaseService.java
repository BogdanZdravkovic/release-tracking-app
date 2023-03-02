package com.bogdan.releasetracking.service;

import com.bogdan.releasetracking.dto.ReleaseRequestWsDTO;
import com.bogdan.releasetracking.model.Release;

import java.util.List;

public interface ReleaseService {

    /**
     * Retrieves list of all Release objects
     * @return List<Release>
     */
    List<Release> getAllReleases();

    /**
     * Retrieves Release object from DB by given identifier
     * @param id
     * @return Release
     */
    Release getReleaseById(Long id);

    /**
     * Retrieves Release object by given identifier and perform update
     * @param id
     * @param releaseWsDTO
     * @return Release
     */
    Release updateRelease(ReleaseRequestWsDTO releaseWsDTO, Long id);

    /**
     * Creates new Release object
     * @param releaseWsDTO
     * @return Release
     */
    Release createRelease(ReleaseRequestWsDTO releaseWsDTO);

    /**
     * Remove Release object with given id from database
     * @param id
     */
    void deleteRelease(Long id);
}
