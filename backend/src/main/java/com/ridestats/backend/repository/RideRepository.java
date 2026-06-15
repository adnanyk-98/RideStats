package com.ridestats.backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ridestats.backend.entity.Ride;

@Repository
public interface RideRepository
        extends JpaRepository<Ride, Long> {

        List<Ride> findAllByOrderByUploadedAtDesc();

        Optional<Ride> findTopByOrderByRideDateDesc();

        List<Ride> findByRideDateGreaterThanEqualAndRideDateLessThan(
                LocalDateTime startInclusive,
                LocalDateTime endExclusive);
}