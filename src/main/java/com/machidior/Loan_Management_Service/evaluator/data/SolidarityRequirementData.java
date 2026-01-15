package com.machidior.Loan_Management_Service.evaluator.data;

import com.machidior.Loan_Management_Service.model.requirement.SolidarityMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SolidarityRequirementData {

    private List<SolidarityMemberData> items;
}