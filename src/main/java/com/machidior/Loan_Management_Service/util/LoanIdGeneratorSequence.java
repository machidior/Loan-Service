package com.machidior.Loan_Management_Service.util;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanIdGeneratorSequence {
    @Id
    private String monthKey;
    private int sequence;
}
