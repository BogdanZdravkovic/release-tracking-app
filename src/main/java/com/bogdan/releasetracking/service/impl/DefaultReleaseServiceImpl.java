package com.bogdan.releasetracking.service.impl;

import com.bogdan.releasetracking.dto.ReleaseRequestWsDTO;
import com.bogdan.releasetracking.exception.ReleaseValidationException;
import com.bogdan.releasetracking.model.Release;
import com.bogdan.releasetracking.model.ReleaseStatus;
import com.bogdan.releasetracking.repository.ReleaseRepository;
import com.bogdan.releasetracking.service.ReleaseService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;


@Service
public class DefaultReleaseServiceImpl  implements ReleaseService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultReleaseServiceImpl.class);

    @Value("${default.date.format:yyyy-MM-dd}")
    private String defaultDateFormat;

    @Autowired
    private ReleaseRepository releaseRepository;

    public DefaultReleaseServiceImpl(ReleaseRepository releaseRepository) {
        this.releaseRepository = releaseRepository;
    }

    @Override
    @CachePut(value = "releases")
    public List<Release> getAllReleases() {
        return releaseRepository.findAll();
    }

    @Override
    @CachePut(value = "releases", key = "#id")
    public Release getReleaseById(Long id) {
        return releaseRepository.findById(id).orElseThrow(() -> new ReleaseValidationException("No release found for given ID.", String.valueOf(id)));
    }

    @Override
    @CachePut(value = "releases", key = "#id")
    public Release updateRelease(ReleaseRequestWsDTO releaseWsDTO, Long id) {
        LOG.info("updating release: " + id);
        Release currentRelease = releaseRepository.findById(id).orElseThrow(RuntimeException::new);
        currentRelease.setName(releaseWsDTO.getName());
        currentRelease.setDescription(releaseWsDTO.getDescription());
        currentRelease.setStatus(Enum.valueOf(ReleaseStatus.class, releaseWsDTO.getStatus().toUpperCase()));
        currentRelease.setReleaseDate(validateRequestRelaseDate(releaseWsDTO.getReleaseDate()));
        currentRelease.setLastUpdateAt(LocalDateTime.now());
        releaseRepository.save(currentRelease);
        return currentRelease;
    }

    @Override
    @CachePut(value = "releases")
    public Release createRelease(ReleaseRequestWsDTO releaseWsDTO) {
        Release newRelease = new Release();
        newRelease.setName(releaseWsDTO.getName());
        newRelease.setDescription(releaseWsDTO.getDescription());
        newRelease.setReleaseDate(validateRequestRelaseDate(releaseWsDTO.getReleaseDate()));
        newRelease.setStatus(ReleaseStatus.CREATED);
        newRelease.setCreatedAt(LocalDateTime.now());
        newRelease.setLastUpdateAt(LocalDateTime.now());
        releaseRepository.save(newRelease);
        LOG.info("new release created: " + newRelease.getId());
        return newRelease;
    }

    @Override
    @CacheEvict(value = "releases", key = "#id")
    public void deleteRelease(Long id) {
        releaseRepository.deleteById(id);
    }

    private LocalDate validateRequestRelaseDate(final String releaseDateString) {
        if (StringUtils.isEmpty(releaseDateString)) {
            throw new ReleaseValidationException("Release date must not be empty!");
        }
        Locale currentLocale = LocaleContextHolder.getLocale();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(defaultDateFormat, currentLocale);
        try {
            LocalDate releaseDate = LocalDate.parse(releaseDateString, formatter);
            return releaseDate;
        } catch (DateTimeParseException e) {
            LOG.error("Release date parsing exception.");
            throw new ReleaseValidationException("release_date attribute has wrong format. Date must be in format: " + defaultDateFormat);
        }
    }
}
