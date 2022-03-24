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

    public void deleteAllTestimonialByUserId(int userId){
        testimonialRepository.deleteTestimonialsByUser_Id(userId);
    }

    public Testimonial findByUserId(int userId){
        return testimonialRepository.findByUser_Id(userId);
    }

    public List<Testimonial> findTestimonialsByUserId(int userId){
        return testimonialRepository.findTestimonialsByUser_Id(userId);
    }


}
