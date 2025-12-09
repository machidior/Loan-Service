package com.machidior.Loan_Management_Service.controller;

import com.machidior.Loan_Management_Service.dtos.*;
import com.machidior.Loan_Management_Service.model.BusinessDetails;
import com.machidior.Loan_Management_Service.model.KuzaBusinessDetails;
import com.machidior.Loan_Management_Service.model.KuzaLoan;
import com.machidior.Loan_Management_Service.service.KuzaLoanApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/loan-application/kuza")
@RequiredArgsConstructor
public class KuzaLoanApplicationController {

    private final KuzaLoanApplicationService service;

    @PostMapping("/create-application-details")
    public ResponseEntity<KuzaLoanApplicationResponse> applyKuzaLoan(@RequestBody ApplicationDetails applicationDetails){
        return ResponseEntity.ok(service.applyKuzaLoan(applicationDetails));
    }

    @PostMapping(path = "/save-business-details/{applicationNumber}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KuzaLoanApplicationResponse> saveBusinessDetails(
            @PathVariable String applicationNumber,
            @RequestPart("bankStatement") MultipartFile bankStatement,
            @RequestPart("insuranceComprehensiveCover") MultipartFile insuranceComprehensiveCover,
            @RequestPart("tinCertificate") MultipartFile tinCertificate,
                @RequestPart("brelaCertificate") MultipartFile brelaCertificate,
            @RequestPart("businessLicense") MultipartFile businessLicense,
            @RequestPart("businessDetails") List<KuzaBusinessDetails> businessDetails) throws IOException {

        return ResponseEntity.ok(service.saveBusinessDetails(applicationNumber,bankStatement,insuranceComprehensiveCover,tinCertificate,brelaCertificate,businessLicense,businessDetails));
    }

    @PostMapping(path = "/save-loan-collaterals/{applicationNumber}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KuzaLoanApplicationResponse> saveLoanCollaterals(
            @PathVariable String applicationNumber,
            @RequestPart("collaterals") List<KuzaLoanCollateralRequest> collaterals,
            @RequestPart("photos") List<MultipartFile> photos) throws IOException {
        return ResponseEntity.ok(service.saveLoanCollaterals(applicationNumber,collaterals,photos));
    }

    @PostMapping(path = "/save-loan-guarantor/{applicationNumber}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KuzaLoanApplicationResponse> saveBusinessGuarantor(
            @PathVariable String applicationNumber,
            @RequestPart("passport") MultipartFile passport,
            @RequestPart("identificationCard") MultipartFile identificationCard,
            @RequestPart("guarantorRequest") KuzaLoanGuarantorRequest guarantorRequest) throws IOException {
        return ResponseEntity.ok(service.saveLoanGuarantor(applicationNumber,passport,identificationCard,guarantorRequest));
    }

    @GetMapping("/apply/{applicationNumber}")
    public ResponseEntity<KuzaLoanApplicationResponse> createApplication(@PathVariable String applicationNumber){
        return ResponseEntity.ok(service.createApplication(applicationNumber));
    }


    @PostMapping("/approve/{applicationNumber}")
    public ResponseEntity<KuzaLoan> approveApplication(@PathVariable String applicationNumber, @RequestBody LoanApplicationApprovalRequest request){
        return ResponseEntity.ok(service.approveKuzaLoanApplication(applicationNumber,request));
    }

    @PostMapping("/reject/{applicationNumber}")
    public ResponseEntity<KuzaLoanApplicationResponse> rejectKuzaLoanApplication(@PathVariable String applicationNumber, @RequestBody String rejectionReason){
        return ResponseEntity.ok(service.returnKuzaLoanApplication(applicationNumber,rejectionReason));
    }

    @PostMapping("/return/{applicationNumber}")
    public ResponseEntity<KuzaLoanApplicationResponse> returnKuzaLoanApplication(@PathVariable String applicationNumber, @RequestBody String reasonOfReturn){
        return ResponseEntity.ok(service.returnKuzaLoanApplication(applicationNumber,reasonOfReturn));
    }

    @GetMapping("/{applicationNumber}")
    public ResponseEntity<KuzaLoanApplicationResponse> getApplicationLoan(@PathVariable String applicationNumber){
        return ResponseEntity.ok(service.getLoanApplication(applicationNumber));
    }
}
