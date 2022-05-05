package com.example.healthadvisors.service;

import com.example.healthadvisors.dto.CreateTestimonialRequest;
import com.example.healthadvisors.entity.Testimonial;
import com.example.healthadvisors.repository.TestimonialRepository;
import com.example.healthadvisors.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestimonialService {

    private final TestimonialRepository testimonialRepository;
    private final ModelMapper modelMapper;

    public void save(Testimonial testimonial){
        testimonialRepository.save(testimonial);
    }

    public void getDeleteTestimonial(Testimonial testimonial){
        testimonialRepository.delete(testimonial);
    }

    public List<Testimonial> findTestimonialsByUserId(int userId){
        return testimonialRepository.findTestimonialsByUser_Id(userId);
    }


    public List<Testimonial> getAllTestimonials() {
        return testimonialRepository.findAll();
    }

    public void saveFromDTO(CreateTestimonialRequest createTestimonialRequest, CurrentUser currentUser) {
        Testimonial newTestimonial = modelMapper.map(createTestimonialRequest, Testimonial.class);
        newTestimonial.setUser(currentUser.getUser());
        testimonialRepository.save(newTestimonial);
    }
}
