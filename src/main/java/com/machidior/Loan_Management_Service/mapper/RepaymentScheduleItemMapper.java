package com.machidior.Loan_Management_Service.mapper;

import com.machidior.Loan_Management_Service.dtos.RepaymentScheduleItemDTO;
import com.machidior.Loan_Management_Service.model.RepaymentSchedule;
import com.machidior.Loan_Management_Service.model.RepaymentScheduleItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RepaymentScheduleItemMapper {

    public RepaymentScheduleItem toEntity(RepaymentScheduleItemDTO dto, RepaymentSchedule schedule){

        return RepaymentScheduleItem.builder()
                .installmentNumber(dto.getInstallmentNumber())
                .dueDate(dto.getDueDate())
                .paymentAmount(dto.getPaymentAmount())
                .principal(dto.getPrincipal())
                .interest(dto.getInterest())
                .loanFee(dto.getLoanFee())
                .repaymentSchedule(schedule)
                .remainingBalance(dto.getRemainingBalance())
                .build();
    }

    public RepaymentScheduleItemDTO toDTO(RepaymentScheduleItem item){

        return RepaymentScheduleItemDTO.builder()
                .installmentNumber(item.getInstallmentNumber())
                .dueDate(item.getDueDate())
                .paymentAmount(item.getPaymentAmount())
                .principal(item.getPrincipal())
                .interest(item.getInterest())
                .loanFee(item.getLoanFee())
                .remainingBalance(item.getRemainingBalance())
                .build();
    }
}
