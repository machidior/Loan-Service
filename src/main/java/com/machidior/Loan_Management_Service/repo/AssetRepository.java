package com.machidior.Loan_Management_Service.repo;

import com.machidior.Loan_Management_Service.model.requirement.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long> {
}
