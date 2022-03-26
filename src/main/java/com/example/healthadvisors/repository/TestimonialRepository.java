package com.example.healthadvisors.repository;

import com.example.healthadvisors.entity.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestimonialRepository extends JpaRepository<Testimonial,Integer> {

    List<Testimonial> findTestimonialsByPatient_Id(int patientId);

    void deleteTestimonialsByPatient_Id(int patientId);

}
