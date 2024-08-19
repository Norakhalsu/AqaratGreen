package com.example.aqaratgreen.Repository;


import com.example.aqaratgreen.Model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Integer> {
    Unit findUnitById(int id);
   // Unit findByName(String name);

    @Query("select u from Unit u where u.offerType=?1 ")//يطلع العميل على الواحدات المعروضه (للايجار)او(للبيع)
    List<Unit> SearchRentOrBuyList(String offerType);

     List<Unit> findUnitsByInvestorId(int investorId);//استعراض الوحدات الخاصة بالمستثمر


     @Query("select u from Unit u where u.criteriaId=?1 ")// حساب الاستدامة لمبنى معين
     Unit checkCriteriaUnit(Integer criteriaId);

     @Query("select u from Unit  u where u.city=?1")
     List<Unit> SearchUnitsByCity(String city);//قائمة بالوحدات السكنية بمدينه معينه



     // Unit findUnitByCity(String city);
   //  List<Unit> findUnitsByCityAndDistrictName(String city , String districtName);// قائمة بالوحدات السكنية في المدينة والحي
}
