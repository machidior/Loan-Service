package com.machidior.Loan_Management_Service.service;

import com.machidior.Loan_Management_Service.dtos.request.*;
import com.machidior.Loan_Management_Service.dtos.response.LoanApplicationResponse;
import com.machidior.Loan_Management_Service.model.Loan;

import java.util.List;

public interface LoanApplicationService {
    LoanApplicationResponse createLoanApplicationDetails(ApplicationDetails request);

    LoanApplicationResponse submitLoan(String applicationNumber);

    Loan approveLoanApplication(String applicationNumber, LoanApplicationApprovalRequest request);

    LoanApplicationResponse rejectLoanApplication(String applicationNumber, String rejectionReason);

    LoanApplicationResponse returnLoanApplication(String applicationNumber, String reasonOfReturn);

    LoanApplicationResponse getLoanApplication(String applicationNumber);

    List<LoanApplicationResponse> getAllLoanApplications();

    void deleteLoanApplication(String applicationNumber);
}
