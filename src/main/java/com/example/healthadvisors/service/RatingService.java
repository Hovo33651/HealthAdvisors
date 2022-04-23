package com.example.healthadvisors.service;

import com.example.healthadvisors.entity.Rating;
import com.example.healthadvisors.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;


    public int getDoctorRating(int doctorId) {
        List<Rating> ratingsByDoctor_id = ratingRepository.findRatingsByDoctor_Id(doctorId);
        List<Integer> ratings = new ArrayList<>();
        for (Rating rating : ratingsByDoctor_id) {
            ratings.add(rating.getRating());
        }
        OptionalDouble average = ratings.stream().mapToInt(r -> r).average();
        return average.isPresent() ? (int)Math.round(average.getAsDouble()) : 0;
    }


    public void save(Rating rating) {
        ratingRepository.save(rating);
    }
}
