package com.machidior.Loan_Management_Service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter@Getter
@Table(name="group_loan")
@Builder
public class GroupLoan {

    @Id
    @GeneratedValue(generator = "group_loan_id_generator")
    @GenericGenerator(
            name = "group_loan_id_generator",
            strategy = "com.machidior.Loan_Management_Service.generator.GroupLoanIdGenerator"
    )
    private String id;
}
