package com.bogdan.releasetracking.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "release")
public class Release {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name="releaseStatus")
    private ReleaseStatus status;

    @NotNull
    private LocalDate releaseDate;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdateAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReleaseStatus getStatus() {
        return status;
    }

    public void setStatus(ReleaseStatus status) {
        this.status = status;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastUpdateAt() {
        return lastUpdateAt;
    }

    public void setLastUpdateAt(LocalDateTime lastUpdateAt) {
        this.lastUpdateAt = lastUpdateAt;
    }
}
