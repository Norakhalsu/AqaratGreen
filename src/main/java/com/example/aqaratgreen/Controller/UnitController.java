package com.example.aqaratgreen.Controller;


import com.example.aqaratgreen.Model.Unit;
import com.example.aqaratgreen.Service.UnitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/unit")
public class UnitController {

    private final UnitService unitService;


    @GetMapping("/get")// get all Units
    public ResponseEntity getUnits() {
        return  ResponseEntity.status(200).body(unitService.getAllUnits());
    }



    @PostMapping("/add")// add new Unit
    public ResponseEntity addUnit(@Valid @RequestBody Unit unit, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        unitService.addUnit(unit);
        return ResponseEntity.status(201).body(" Unit Added Successfully");
    }


    @PutMapping("/update/{id}")// update Unit by id
    public ResponseEntity updateUnit(@PathVariable Integer id ,@Valid@RequestBody Unit unit ,Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        unitService.updateUnit(id,unit);
        return ResponseEntity.status(201).body("Unit Updated Successfully");
    }


    @DeleteMapping("/delete/{id}")// delete Unit by id
    public ResponseEntity deleteUnit(@PathVariable Integer id){
        unitService.deleteUnit(id);
        return ResponseEntity.status(201).body("Unit Deleted Successfully");
    }

    // ------------------------------ End Points ----------------------------

    @GetMapping("/calculateRentCost/{unitId}/{additionalRoom}")//حساب تكلفة الإيجار الشهري بناءً على عدد الغرف الإضافية
    public ResponseEntity<Double> calculateMonthlyRentCost(@PathVariable Integer unitId, @PathVariable int additionalRoom) {
        return ResponseEntity.status(200).body(unitService.calculateMonthlyRentCost(unitId ,additionalRoom));
    }

    @GetMapping("/sustainability/{unitId}")// حساب مستوى الاستدامة لمبنى معين
    public ResponseEntity<String> calculateSustainabilityIndex(@PathVariable Integer unitId) {
        return ResponseEntity.status(200).body(unitService.calculateSustainabil(unitId));
    }

   @PostMapping("/check/{clientId}/{unitId}") //حساب المبلغ الذي دفعه العميل والمبلغ المتبقي له لدفعه لوحدة معينة
   private ResponseEntity PaymentAndRemainAmountForUnit(@PathVariable Integer clientId,@PathVariable Integer  unitId){
     return ResponseEntity.status(200).body(unitService.PaymentAndRemainAmountForUnit(clientId,unitId));
     }

     @GetMapping("/expected-Units")//توقعات الارباح للوحدات السكنية بناء على المدينه و الحي و المساحةوالسعر :خلال السنه الحالية
     public ResponseEntity expectedUnits() {
        return ResponseEntity.status(200).body(unitService.expectedProfitUnitPerYear());
     }



}
