package com.example.healthadvisors.service;

import com.example.healthadvisors.entity.Testimonial;
import com.example.healthadvisors.repository.TestimonialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestimonialService {

    private final TestimonialRepository testimonialRepository;

    public void save(Testimonial testimonial){
        testimonialRepository.save(testimonial);
    }

    public void getDeleteTestimonial(Testimonial testimonial){
        testimonialRepository.delete(testimonial);
    }

    public List<Testimonial> findTestimonialsByPatientId(int userId){
        return testimonialRepository.findTestimonialsByUser_Id(userId);
    }


}
