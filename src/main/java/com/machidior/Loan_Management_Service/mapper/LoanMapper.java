package com.machidior.Loan_Management_Service.mapper;

import com.machidior.Loan_Management_Service.dtos.CollateralDTO;
import com.machidior.Loan_Management_Service.dtos.GuarantorDTO;
import com.machidior.Loan_Management_Service.dtos.LoanApplicationRequest;
import com.machidior.Loan_Management_Service.dtos.LoanApplicationResponse;
import com.machidior.Loan_Management_Service.enums.LoanProductType;
import com.machidior.Loan_Management_Service.enums.RepaymentFrequency;
import com.machidior.Loan_Management_Service.model.Collateral;
import com.machidior.Loan_Management_Service.model.Guarantor;
import com.machidior.Loan_Management_Service.model.LoanApplication;

import java.math.BigDecimal;
import java.util.stream.Collectors;

public class LoanMapper {

    public static LoanApplication toEntity(LoanApplicationRequest dto) {
        LoanApplication loan = new LoanApplication();
        loan.setCustomerId(dto.getCustomerId());
        loan.setLoanProductType(LoanProductType.valueOf(dto.getLoanProductType()));
        loan.setAmountRequested(dto.getAmountRequested());
        loan.setTermMonths(dto.getTermMonths());
        loan.setRepaymentFrequency(RepaymentFrequency.valueOf(dto.getRepaymentFrequency()));
        loan.setPurpose(dto.getPurpose());
        loan.setLoanOfficerId(dto.getLoanOfficerId());
        return loan;
    }

    public static LoanApplicationResponse toResponse(LoanApplication loan) {
        return LoanApplicationResponse.builder()
                .applicationNumber(loan.getApplicationNumber())
                .customerId(loan.getCustomerId())
                .loanProductType(String.valueOf(loan.getLoanProductType()))
                .amountRequested(loan.getAmountRequested())
                .amountApproved(loan.getAmountApproved())
                .status(loan.getStatus().name())
                .createdAt(loan.getCreatedAt())
                .collaterals(loan.getCollaterals() != null
                        ? loan.getCollaterals().stream().map(LoanMapper::toCollateralDTO).collect(Collectors.toList())
                        : null)
                .guarantors(loan.getGuarantors() != null
                        ? loan.getGuarantors().stream().map(LoanMapper::toGuarantorDTO).collect(Collectors.toList())
                        : null)
                .build();
    }

    private static CollateralDTO toCollateralDTO(Collateral c) {
        return CollateralDTO.builder()
                .type(c.getType())
                .description(c.getDescription())
                .document(c.getDocument())
                .valuation(BigDecimal.valueOf(c.getValuation()))
                .build();
    }

    private static GuarantorDTO toGuarantorDTO(Guarantor g) {
        return GuarantorDTO.builder()
                .name(g.getName())
                .relationship(g.getRelationship())
                .phoneNumber(g.getPhoneNumber())
                .nationalId(g.getNationalId())
                .address(g.getAddress())
                .document(g.getDocument())
                .build();
    }
}
