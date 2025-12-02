package com.machidior.Loan_Management_Service.service;


import com.machidior.Loan_Management_Service.repo.BusinessLoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BusinessLoanService  {

    private final BusinessLoanRepository businessLoanRepository;

}
