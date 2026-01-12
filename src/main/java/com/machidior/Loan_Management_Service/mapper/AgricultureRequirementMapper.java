package com.machidior.Loan_Management_Service.mapper;

import com.machidior.Loan_Management_Service.dtos.request.AgricultureApplicationRequest;
import com.machidior.Loan_Management_Service.enums.RequirementType;
import com.machidior.Loan_Management_Service.evaluator.data.AgricultureRequirementData;
import com.machidior.Loan_Management_Service.model.AgricultureRequirementDetails;
import org.springframework.stereotype.Component;

@Component
public class AgricultureRequirementMapper {

    public AgricultureRequirementDetails toEntity(AgricultureApplicationRequest request) {
        AgricultureRequirementDetails entity = new AgricultureRequirementDetails();
        entity.setType(RequirementType.AGRICULTURE);
        entity.setFarmLocation(request.getFarmLocation());
        entity.setCropType(request.getCropType());
        entity.setFarmSize(request.getFarmSize());
        entity.setFarmSizeUnit(request.getFarmSizeUnit());
        entity.setLandOwnershipType(request.getLandOwnershipType());
        entity.setProductionCycle(request.getProductionCycle());
        entity.setProductionDurationMonths(request.getProductionDurationMonths());
        entity.setExpectedYield(request.getExpectedYield());
        entity.setYieldUnit(request.getYieldUnit());
        entity.setHasOffTaker(request.getHasOffTaker());
        entity.setOffTakerName(request.getOffTakerName());
        entity.setOffTakerContractRef(request.getOffTakerContractRef());
        entity.setInspectionDate(request.getInspectionDate());
        entity.setInspectionDate(request.getInspectionDate());
        entity.setInspectorName(request.getInspectorName());
        entity.setInspectionRemarks(request.getInspectionRemarks());
        entity.setFarmDetailsProvided(request.getFarmDetailsProvided());
        entity.setProductionCycleProvided(request.getProductionCycleProvided());
        entity.setOffTakerAgreementProvided(request.getOffTakerAgreementProvided());
        entity.setFarmInspected(request.getFarmInspected());
        return entity;
    }

    public AgricultureRequirementData toEvaluationData(AgricultureApplicationRequest request) {
        AgricultureRequirementData data = new AgricultureRequirementData();
        data.setFarmSize(request.getFarmSize());
        data.setFarmDetailsProvided(request.getFarmDetailsProvided());
        data.setProductionCycleProvided(request.getProductionCycleProvided());
        data.setOffTakerAgreementProvided(request.getOffTakerAgreementProvided());
        data.setFarmInspectionCompleted(request.getFarmInspected());
        return data;
    }
}
