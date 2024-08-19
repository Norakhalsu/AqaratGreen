package com.example.aqaratgreen.Model;


import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.hibernate.validator.constraints.ISBN;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Contract {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    @NotEmpty(message = "please -Contract Type- is Required ")
    @Pattern(regexp = "^(sale|rent)$", message = "please -Contract Type- must be 'sale' or 'rent' ")
    //@Column(columnDefinition = "varchar() NOT NULL CHECK(ContractType='sale' or ContractType='rent')  ")
    private String contractType;

    @NotNull(message = "Please -Start Date- of Contract is Required ")
    @Column(columnDefinition = "datetime NOT NULL")
    private LocalDate startDate;

    @NotNull(message = "Please -End Date- of Contract is Required ")
     @Column(columnDefinition = "datetime NOT NULL")
     private LocalDate endDate;

    @NotEmpty(message = "please -Description of unit- is Required ")
    @Size(min = 10,max = 100 , message = "please 'Description length' must 10-100 characters")
    @Column(columnDefinition = "varchar(100) NOT NULL")
     private String description;

    @NotEmpty(message = "Please -pay State- is Required ")
    @Pattern(regexp = "^(paid|unpaid)$", message = "Please -Pay State- must be 'Paid' or 'Unpaid'")
    //@Column(columnDefinition = "varchar(5) NOT NULL CHECK(payState='paid' or payState='unpaid')")
    private String payState;

     @NotEmpty(message = "Please -Estate Mediator Name- is Required ")
     @Column(columnDefinition = "varchar(35) NOT NULL")
     private String estateMediatorName;

     @Pattern(regexp = "^(valid|expired)$", message = "Please -State Contract- must be 'Valid' or 'Expired' ")
     //@Column(columnDefinition = "varchar(7) NOT NULL CHECK(stateContract='valid' or stateContract='expired')")
     private String stateContract;

     @NotNull(message = "Please -Advance payment in months- is Required ")
     @Column(columnDefinition = "int NOT NULL")
     private int advancePaymentInMonths;

     @NotNull(message = "Please -Total Contract- is Required ")
     @Column(columnDefinition = "double NOT NULL")
     private double totalContract;

     private double amountPaid;

     @AssertTrue(message = "Please -terms and Conditions- must be True")
     //@Column(columnDefinition = "boolean default true CHECK( termsAndConditions=true ) ")
     private boolean termsAndConditions;


     @NotNull(message = " Please -Unit Id- is Required ")
     @Column(columnDefinition = "int NOT NULL")
     private Integer unitId;

    @NotNull(message = "Please -Criteria Id- is Required ")
    @Column(columnDefinition = "int NOT NULL")
    private Integer criteriaId;

    @NotNull(message = "Please -Client Id- is Required ")
    @Column(columnDefinition = "int NOT NULL")
    private Integer clientId;

    @NotNull(message = "Please -Investor Id- is Required ")
    @Column(columnDefinition = "int NOT NULL")
    private Integer investorId;

}
