package com.bogdan.releasetracking.controller;

import com.bogdan.releasetracking.model.Release;
import com.bogdan.releasetracking.repository.ReleaseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/releases")
public class ReleaseTrackingController {

    private final ReleaseRepository releaseRepository;

    public ReleaseTrackingController(ReleaseRepository releaseRepository) {
        this.releaseRepository = releaseRepository;
    }

    @GetMapping
    public List<Release> getAllReleases() {
        return releaseRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Release> getReleaseById(@PathVariable Long id) {
        Optional<Release> release = releaseRepository.findById(id);
        if (release.isPresent()) {
            return ResponseEntity.ok(release.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Release> createRelease(@Valid @RequestBody Release release) {
        Release newRelease = releaseRepository.save(release);
        return ResponseEntity.created(URI.create("/releases/" + newRelease.getId())).body(newRelease);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Release> updateRelease(@PathVariable Long id, @Valid @RequestBody Release release) {
        Release currentRelease = releaseRepository.findById(id).orElseThrow(RuntimeException::new);
        currentRelease.setName(release.getName());
        currentRelease.setDescription(release.getDescription());
        currentRelease = releaseRepository.save(currentRelease);

        return ResponseEntity.ok(currentRelease);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRelease(@PathVariable Long id) {
        releaseRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
