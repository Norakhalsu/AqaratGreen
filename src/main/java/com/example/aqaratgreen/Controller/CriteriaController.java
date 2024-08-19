package com.example.aqaratgreen.Controller;


import com.example.aqaratgreen.Model.Criteria;
import com.example.aqaratgreen.Model.Unit;
import com.example.aqaratgreen.Service.CriteriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/criteria")
public class CriteriaController {

    private final CriteriaService criteriaService;

    @GetMapping("/get")// get all criteria
    public ResponseEntity getCriteria() {
        return  ResponseEntity.status(200).body(criteriaService.getAllCriteria());
    }


    @PostMapping("/add")// add new criteria
    public ResponseEntity addCriteria(@Valid @RequestBody Criteria criteria, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        criteriaService.addCriteria(criteria);
        return ResponseEntity.status(201).body(" Criteria Added Successfully");
    }


    @PutMapping("/update/{id}")// update criteria by id
    public ResponseEntity updateCriteria(@PathVariable Integer id ,@Valid@RequestBody Criteria criteria ,Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        criteriaService.updateCriteria(id,criteria);
        return ResponseEntity.status(201).body("Criteria Updated Successfully");
    }


    @DeleteMapping("/delete/{id}")// delete criteria by id
    public ResponseEntity deleteCriteria(@PathVariable Integer id){
        criteriaService.deleteCriteria(id);
        return ResponseEntity.status(201).body("Criteria Deleted Successfully");
    }

   // ------------------------- End Point -----------------------
    @GetMapping("/carbonTax/{unitId}/{investorId}/{criteriaId}") // تشيك على الوحدة السكنية اذا استخدام الكربون يتم فرض الضرائب
    public ResponseEntity carbonTax(@PathVariable Integer unitId,@PathVariable Integer investorId,@PathVariable Integer criteriaId) {
        return ResponseEntity.status(200).body(criteriaService.calculateCarbonTax(unitId,investorId,criteriaId));
    }


}
