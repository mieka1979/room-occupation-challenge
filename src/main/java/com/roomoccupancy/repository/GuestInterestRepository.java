package com.roomoccupancy.repository;

import com.roomoccupancy.model.GuestInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface GuestInterestRepository extends JpaRepository<GuestInterest, Long> {

    List<GuestInterest> findByDate(Date date);

    @Query("SELECT max(date) FROM GuestInterest")
    Date findLatestDate();
}
