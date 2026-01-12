package com.machidior.Loan_Management_Service.feign;

import com.machidior.Loan_Management_Service.feign.policy.Policies;
import com.machidior.Loan_Management_Service.feign.requirement.Requirements;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
    name = "loan-configuration-service",
    url = "${feign.clients.loan-configuration-service.url}/api/internal/product-configurations"
)
public interface ProductConfigurationsClient {

    @GetMapping("/requirements/{productVersionId}")
    public Requirements getVersionRequirements(@PathVariable Long productVersionId);


    @GetMapping("/policies/{productVersionId}")
    public Policies getVersionPolicies(@PathVariable Long productVersionId);

    @GetMapping("/charges/{productVersionId}")
    public List<ProductCharge> getVersionCharges(@PathVariable Long productVersionId);
}
