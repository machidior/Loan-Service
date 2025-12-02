package com.machidior.Loan_Management_Service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "repayment_schedules")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepaymentSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loanId;
    @OneToMany(mappedBy = "repaymentSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RepaymentScheduleItem> scheduleItems;
}
