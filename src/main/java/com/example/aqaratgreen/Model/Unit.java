package com.example.aqaratgreen.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @NotEmpty(message = "Please 'Name of Unit' is Required ")
    @Size(min = 3 , max = 20,message = "Please 'Name of Unit' length must be 3-20 characters  ")
    @Column(columnDefinition = "varchar(20) NOT NULL ")
    private String name;


    @NotEmpty(message = "please 'Type of Unit' is Required")
    @Size(min = 5, max = 9, message = "Type of Unit must be 5-9 characters")
    @Pattern(regexp = "^(villa|apartment|studio)$", message = "Type must be 'Villa' or 'Apartment' or 'Studio'")
    //@Column(columnDefinition = "varchar(9) NOT NULL CHECK(type='villa' or type='apartment' or type='studio') ")
    private String type;


    @NotEmpty(message = "Offer Type is Required")
    @Pattern(regexp = "^(rent|buy|Transfer-of-ownership)$", message = "Type must be 'Rent' , 'Buy' or 'Transfer-of-ownership'") // Rent or buy
   // @Column(columnDefinition = "varchar(4) NOT NULL CHECK(offerType='rent' or offerType='buy' or offerType='Transfer-of-ownership' ) ")
    private String offerType;

    @NotEmpty(message = "please 'Description of unit' is Required ")
    @Size(min = 10,max = 100 , message = "please 'Description length' must 10-100 characters")
    @Column(columnDefinition = "varchar(100) NOT NULL")
    private String description;

    @NotEmpty(message = "please 'City of unit' is Required ")
    @Column(columnDefinition = "varchar(20) NOT NULL")
    private String city;

    @NotEmpty(message = "please 'Address of unit' is Required ")
    @Column(columnDefinition = "varchar(20) NOT NULL")
    private String address;

    @NotEmpty(message = "please 'District Name of unit' is Required ")
    @Column(columnDefinition = "varchar(20) NOT NULL")
    private String districtName;

    @NotNull(message = "Please 'Area of Unit' is Required ")
    @Column(columnDefinition = "double NOT NULL")
    private double area;

    @NotNull(message = "please 'rooms number' is required")
    @Min(value = 1 , message = " Minimum room number is 1")
    @Max(value = 50 , message = "Maximum room number is 50 ")
    @Column(columnDefinition = "int(50) NOT NULL ")
    //@Pattern(regexp = "^[0-9]+$", message = "Room number must contain only numbers")
    private int rooms;

    @NotNull(message = "Please 'Price of Unit' is Required ")
    @Column(columnDefinition = "double NOT NULL")
    private double price;

    @Column(columnDefinition = "boolean default true")
    private boolean availability=true;

    @Column(columnDefinition = "double NOT NULL")
    private double totalAmountPaid; // المبلغ الإجمالي المدفوع

    @Column(columnDefinition = "double NOT NULL")
    private double totalAmountDue; // المبلغ الإجمالي المستحق الدفع

    @Column(columnDefinition = "double NOT NULL")
    private double remainingAmount; // المبلغ المتبقي للدفع

    @Column(columnDefinition = "datetime")// تاريخ سداد الدفعه الاقادمة
    private LocalDate nextPaymentDate;

    @Positive(message = " Criteria Id must positive number")
    private Integer criteriaId;
    @Positive(message = " Criteria Id must positive number")
    private Integer investorId;
    @Positive(message = " Criteria Id must positive number")
    private Integer clientId;

    private double totalTaxes=0;


}
