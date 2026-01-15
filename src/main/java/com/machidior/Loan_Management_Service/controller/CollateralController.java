package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.dtos.request.CollateralRequest;
import com.machidior.Loan_Management_Service.dtos.response.RequirementSubmissionResponse;
import com.machidior.Loan_Management_Service.service.CollateralService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/collaterals")
@RequiredArgsConstructor
public class CollateralController {

    private final CollateralService service;

    @PostMapping(path = "/save-collaterals/{applicationNumber}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RequirementSubmissionResponse> saveCollaterals(
            @PathVariable String applicationNumber,
            @RequestPart("ownershipProof") List<MultipartFile> ownershipProof,
            @RequestPart("collaterals") List<CollateralRequest> collaterals,
            @RequestPart("photos") List<MultipartFile> photos,
            @RequestPart("insuranceDocuments") List<MultipartFile> insuranceDocuments
    ) throws IOException {
        return ResponseEntity.ok(service.saveCollaterals(applicationNumber,collaterals,photos, ownershipProof,insuranceDocuments));
    }

}
