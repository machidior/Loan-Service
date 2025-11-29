package com.machidior.Loan_Management_Service.dtos;

import com.machidior.Loan_Management_Service.enums.LoanProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisbursementResponse {
    private Long id;
    private String loanId;
    private LoanProductType loanProductType;
    private String accountNumber;
    private String disbursementMethod;
    private LocalDate disbursementDate;
    private String disbursedBy;
    private LocalDateTime disbursedAt;
    private LocalDateTime modifiedAt;


}
