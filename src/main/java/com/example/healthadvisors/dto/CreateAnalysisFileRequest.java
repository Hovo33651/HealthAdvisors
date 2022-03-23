package com.example.healthadvisors.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAnalysisFileRequest {

    private String name;
    private int medReportId;
}
