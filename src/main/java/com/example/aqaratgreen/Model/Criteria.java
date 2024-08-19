package com.example.aqaratgreen.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Criteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Min(value = 5, message = "Energy Efficiency Rating Minimum is '5' ")
    //@Column(columnDefinition = "double CHECK( EnergyEfficiencyRating >=5) ")
    private double energyEfficiencyRating; // ممثلة لتقييم كفاءة الطاقة المتوسط

    @AssertTrue(message = "LEED System must be 'True' ")
    //@Column(columnDefinition = "boolean NOT NULL CHECK(LeedSystem=true )")
    private boolean leedSystem;//أشهر أنظمة التصنيف للعقارات المستدامة

    @NotNull(message = " Passive House is Required")
    @Column(columnDefinition = "boolean NOT NULL")
    private boolean  passiveHouse; // العزل الحراري والتهوية الجيدة بحيث تقلل استهلاك الطاقة

    @AssertTrue(message = "Recycling Facilities must be 'True' ")
    //@Column(columnDefinition = "boolean CHECK(RecyclingFacilities = ture ) ")
    private boolean recyclingFacilities;

    @AssertTrue(message = "Solar Panels Must be 'True' ")
   // @Column(columnDefinition = "boolean CHECK(SolarPanels = ture ) ")
    private boolean solarPanels;

    @AssertTrue(message = "Water Conservation Features must be 'True'")
    //@Column(columnDefinition = "boolean CHECK(WaterConservationFeatures = true )")
    private boolean waterConservationFeatures;

    @NotNull(message = "Please -carbon- is Required (true or false) ")
    //@Column(columnDefinition = "boolean CHECK(carbon= false) ")
    private boolean carbon=false;

     private double carbonCo2=350;

    @Pattern(regexp = "^(Eco-Friendly)$", message = "Green Building Materials must be 'Eco-Friendly' ")
    @NotNull(message = "green Building Materials Required")
    //@Column(columnDefinition = "varchar(12) CHECK( GreenBuildingMaterials = 'Eco-Friendly') ")
    private String greenBuildingMaterials;

//    @AssertTrue(message = "Recycling Facilities must be 'True' ")
//    public boolean isRecyclingFacilities;


}
