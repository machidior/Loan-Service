package com.machidior.Loan_Management_Service.service;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final Path guarantorFilesLocation;
    private final Path collateralFilesLocation;
    private final Path businessFileLocation;
    private final Path jobFileLocation;
    private final Path loanContractLocation;

    @Autowired
    public FileStorageService(
        @Value("${file.upload.guarantor-dir}") String guarantorDir,
        @Value("${file.upload.collateral-dir}") String collateralDir,
        @Value("${file.upload.business-dir}") String businessDir,
        @Value("${file.upload.job-dir}") String jobDir,
        @Value("${file.upload.loan-contract-dir}") String loanContractDir
    ) throws IOException {
        this.guarantorFilesLocation = Paths.get(guarantorDir).toAbsolutePath().normalize();
        this.collateralFilesLocation = Paths.get(collateralDir).toAbsolutePath().normalize();
        this.businessFileLocation = Paths.get(businessDir).toAbsolutePath().normalize();
        this.jobFileLocation = Paths.get(jobDir).toAbsolutePath().normalize();
        this.loanContractLocation = Paths.get(loanContractDir).toAbsolutePath().normalize();

        Files.createDirectories(this.guarantorFilesLocation);
        Files.createDirectories(this.collateralFilesLocation);
        Files.createDirectories(this.businessFileLocation);
        Files.createDirectories(this.jobFileLocation);
        Files.createDirectories(this.loanContractLocation);
    }

    public String saveBusinessFiles(MultipartFile file, String applicationNumber, String documentType) throws IOException {
        String fileName = applicationNumber + "_" + documentType + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path targetLocation = this.businessFileLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    public String saveGuarantorFiles(MultipartFile file, String applicationNumber, String documentType) throws IOException {
        String fileName = applicationNumber + "_" + documentType + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path targetLocation = this.guarantorFilesLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    public String saveLoanContractFiles(MultipartFile file, String loanId, String documentType) throws IOException {
        String fileName = loanId + "_" + documentType + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path targetLocation = this.loanContractLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    public String saveCollateralFiles(MultipartFile file, String applicationNumber, String documentType) throws IOException {
        String fileName = applicationNumber + "_" + documentType + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path targetLocation = this.collateralFilesLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    public String saveJobFiles(MultipartFile file, String applicationNumber, String documentType) throws IOException {
        String fileName = applicationNumber + "_" + documentType + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path targetLocation = this.jobFileLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    public org.springframework.core.io.Resource loadBusinessFile(String fileName) throws MalformedURLException {
        Path filePath = this.businessFileLocation.resolve(fileName).normalize();
        org.springframework.core.io.Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists()) {
            return  resource;
        } else {
            throw new RuntimeException("File not found: " + fileName);
        }
    }
    public org.springframework.core.io.Resource loadCollateralFile(String fileName) throws MalformedURLException {
        Path filePath = this.collateralFilesLocation.resolve(fileName).normalize();
        org.springframework.core.io.Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists()) {
            return  resource;
        } else {
            throw new RuntimeException("File not found: " + fileName);
        }
    }
    public org.springframework.core.io.Resource loadGuarantorFile(String fileName) throws MalformedURLException {
        Path filePath = this.guarantorFilesLocation.resolve(fileName).normalize();
        org.springframework.core.io.Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists()) {
            return  resource;
        } else {
            throw new RuntimeException("File not found: " + fileName);
        }
    }
    public org.springframework.core.io.Resource loadJobFile(String fileName) throws MalformedURLException {
        Path filePath = this.jobFileLocation.resolve(fileName).normalize();
        org.springframework.core.io.Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists()) {
            return  resource;
        } else {
            throw new RuntimeException("File not found: " + fileName);
        }
    }
    public void deleteBusinessFile (String fileName) throws IOException {
        Path filePath = this.businessFileLocation.resolve(fileName).normalize();
        Files.delete(filePath);
    }
    public void deleteCollateralFile (String fileName) throws IOException {
        Path filePath = this.collateralFilesLocation.resolve(fileName).normalize();
        Files.delete(filePath);
    }
    public void deleteGuarantorFile (String fileName) throws IOException {
        Path filePath = this.guarantorFilesLocation.resolve(fileName).normalize();
        Files.delete(filePath);
    }
    public void deleteJobFile (String fileName) throws IOException {
        Path filePath = this.jobFileLocation.resolve(fileName).normalize();
        Files.delete(filePath);
    }
}
