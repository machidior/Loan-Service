package com.machidior.Loan_Management_Service.model.requirement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String businessName;
    private String businessType;
    private String businessAddress;
    private String businessSector;
    private Integer yearsInOperation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_details_id", referencedColumnName = "id")
    @JsonIgnore
    private BusinessDetails businessDetails;
}
