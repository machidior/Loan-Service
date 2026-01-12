package com.machidior.Loan_Management_Service.service;

import org.springframework.stereotype.Service;

@Service
public class AgricultureDetailsService {


//    public LoanApplicationResponse saveAgriculturalDetails(
//            String applicationNumber,
//            MultipartFile farmOwnershipDocument,
//            MultipartFile landInspectionReport,
//            MultipartFile cropPhotograph,
//            AgriculturalDetails agriculturalDetails
//    ) throws IOException {
//        LoanApplication application = repository.findById(applicationNumber)
//                .orElseThrow(() -> new ResourceNotFoundException("Loan Application with the given application number is not found!"));
//
//        application.setAgriculturalDetails(agriculturalDetails);
//        LoanApplication updatedApplication = repository.save(application);
//
//        String farmOwnershipDocumentUrl = fileStorageService.saveAgriculturalFiles(farmOwnershipDocument, updatedApplication.getApplicationNumber(),"FARM-OWNERSHIP-DOCUMENT");
//        String landInspectionReportUrl = fileStorageService.saveAgriculturalFiles(landInspectionReport, updatedApplication.getApplicationNumber(),"LAND-INSPECTION-REPORT");
//        String cropPhotographUrl = fileStorageService.saveAgriculturalFiles(cropPhotograph, updatedApplication.getApplicationNumber(),"CROP-PHOTOGRAPH");
//
//        updatedApplication.getAgriculturalDetails().setFarmOwnershipDocumentUrl(farmOwnershipDocumentUrl);
//        updatedApplication.getAgriculturalDetails().setLandInspectionReportUrl(landInspectionReportUrl);
//        updatedApplication.getAgriculturalDetails().setCropPhotographUrl(cropPhotographUrl);
//
//        return mapper.toResponse(repository.save(updatedApplication));
//    }
}
