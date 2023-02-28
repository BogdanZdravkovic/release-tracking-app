package com.bogdan.releasetracking.repository;

import com.bogdan.releasetracking.model.Release;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReleaseRepository extends JpaRepository<Release, Long> {
}
