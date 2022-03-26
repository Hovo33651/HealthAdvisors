package com.example.healthadvisors.service;

import com.example.healthadvisors.entity.Rating;
import com.example.healthadvisors.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;


    public double getDoctorRating(int doctorId){
        List<Integer> ratings = new ArrayList<>();
        for (Rating rating : ratingRepository.findRatingsByDoctor_Id(doctorId)) {
            ratings.add(rating.getRating());
        }
        Stream<Integer> stream = ratings.stream();
        OptionalDouble average = stream.mapToInt(r -> r).average();
        if(average.isPresent()){
            return average.getAsDouble();
        }
        return 0;
    }

    public void setRatingToDoctor(Rating rating){
        ratingRepository.save(rating);
    }
}
