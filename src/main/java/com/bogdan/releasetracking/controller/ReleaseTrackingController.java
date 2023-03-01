package com.bogdan.releasetracking.controller;

import com.bogdan.releasetracking.dto.UpdateReleaseWsDTO;
import com.bogdan.releasetracking.model.Release;
import com.bogdan.releasetracking.service.ReleaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Objects;

@RestController
@RequestMapping("/releases")
public class ReleaseTrackingController {

    @Autowired
    private ReleaseService releaseService;

    @GetMapping
    @Operation(summary = "Get all releases")
    @ApiResponse(responseCode = "200", description = "OK")
    public List<Release> getAllReleases() {
        return releaseService.getAllReleases();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get release by ID")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Release not found")
    public ResponseEntity<Release> getReleaseById(@PathVariable Long id) {
        Release release = releaseService.getReleaseById(id);
        if (Objects.nonNull(release)) {
            return ResponseEntity.ok(release);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create release")
    @ApiResponse(responseCode = "201", description = "Created")
    public ResponseEntity<Release> createRelease(@Valid @RequestBody UpdateReleaseWsDTO releaseWsDTO) {
        Release newRelease = releaseService.createRelease(releaseWsDTO);
        return ResponseEntity.created(URI.create("/releases/" + newRelease.getId())).body(newRelease);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update release")
    @ApiResponse(responseCode = "201", description = "Updated")
    @ApiResponse(responseCode = "404", description = "Release not found")
    public ResponseEntity<Release> updateRelease(@PathVariable Long id, @Valid @RequestBody UpdateReleaseWsDTO releaseWsDTO) {
        Release currentRelease = releaseService.updateRelease(releaseWsDTO, id);
        return ResponseEntity.ok(currentRelease);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete release")
    @ApiResponse(responseCode = "201", description = "Deleted")
    public ResponseEntity<Void> deleteRelease(@PathVariable Long id) {
        releaseService.deleteRelease(id);
        return ResponseEntity.noContent().build();
    }
}
