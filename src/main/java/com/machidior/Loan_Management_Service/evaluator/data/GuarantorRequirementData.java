package com.machidior.Loan_Management_Service.evaluator.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GuarantorRequirementData {
    private List<GuarantorItemData> guarantors = new ArrayList<>();
}
