package com.example.healthadvisors.repository;

import com.example.healthadvisors.entity.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestimonialRepository extends JpaRepository<Testimonial,Integer> {

    List<Testimonial> findTestimonialsByUser_Id(int userId);

    void deleteTestimonialsByUser_Id(int userId);

}
