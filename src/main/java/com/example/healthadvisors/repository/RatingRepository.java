package com.example.healthadvisors.repository;

import com.example.healthadvisors.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Integer> {

    List<Rating> findRatingsByDoctor_Id(int doctorId);


}
