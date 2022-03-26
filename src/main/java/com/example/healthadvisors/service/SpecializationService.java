package com.example.healthadvisors.service;

import com.example.healthadvisors.dto.CreateSpecializationRequest;
import com.example.healthadvisors.entity.Specialization;
import com.example.healthadvisors.repository.SpecializationRepository;
import com.example.healthadvisors.util.FileUploadDownLoadUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SpecializationService {

    private final SpecializationRepository specializationRepository;
    private final FileUploadDownLoadUtils fileUploadUtils;

    private final ModelMapper modelMapper;
    @Value("${health.advisors.specialization.icons.path}")
    String specIconPath;


    public void save(CreateSpecializationRequest createSpecializationRequest, MultipartFile[] uploadedFiles) throws IOException {
        Specialization specialization = modelMapper.map(createSpecializationRequest, Specialization.class);
        specialization.setIconUrl(fileUploadUtils.uploadImage(uploadedFiles,specIconPath));
        specializationRepository.save(specialization);
    }
}

