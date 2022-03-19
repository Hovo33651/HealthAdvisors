package com.example.healthadvisors.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="spaecialization")
public class Specialisation {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
}
