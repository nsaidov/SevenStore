package com.sevenstore.service;

import com.sevenstore.entity.Unit;
import com.sevenstore.repository.UnitRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UnitService {

    private UnitRepository unitRepository;

    public UnitService(UnitRepository unitRepository){
        this.unitRepository = unitRepository;
    }

    @Transactional
    public Unit createUnit(@RequestBody Unit unit){

        if (unit.getName() == null || unit.getName().equals("")){
            throw new IllegalArgumentException("Название не заполнено");
        }

        return unitRepository.save(unit);
    }
}
