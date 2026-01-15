package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.dtos.request.GuarantorRequest;
import com.machidior.Loan_Management_Service.dtos.response.RequirementSubmissionResponse;
import com.machidior.Loan_Management_Service.service.GuarantorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/guarantors")
@RequiredArgsConstructor
public class GuarantorController {

    private final GuarantorService service;

    @PostMapping(path = "/save-guarantor/{applicationNumber}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RequirementSubmissionResponse> saveGuarantor(
            @PathVariable String applicationNumber,
            @RequestPart("passport") List<MultipartFile> passportPhotos,
            @RequestPart("identificationCard") List<MultipartFile> identificationCards,
            @RequestPart("guarantorConsents") List<MultipartFile> guarantorConsents,
            @RequestPart("incomeProofs") List<MultipartFile> incomeProofs,
            @RequestPart("guarantorRequest") List<GuarantorRequest> guarantorRequests
    ) throws IOException {
        return ResponseEntity.ok(service.saveGuarantor(applicationNumber,passportPhotos,identificationCards,guarantorConsents,incomeProofs,guarantorRequests));
    }

}
