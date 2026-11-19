package com.sevenstore.service;

import com.sevenstore.entity.Unit;
import com.sevenstore.repository.UnitRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.StringJoiner;

@Service
public class UnitService {

    private final UnitRepository unitRepository;

    public UnitService(UnitRepository unitRepository){
        this.unitRepository = unitRepository;
    }

    @Transactional
    public String createUnit(@RequestBody Unit unit){

        StringJoiner resultOfRequest = new StringJoiner("; ");
        if (!validateInputData(unit)){
           resultOfRequest.add("Название не заполнено");
        }

        if (resultOfRequest.length() > 0){
            return resultOfRequest.toString();
        }

        unit.setCode(String.format("%03d",unitRepository.count()+1));

        unitRepository.save(unit);

        return "";
    }

    @Transactional
    public String editUnit(@RequestBody Unit unit){

        StringJoiner resultOfRequest = new StringJoiner("; ");
        if (!validateInputData(unit)){
            resultOfRequest.add("Название не заполнено").toString();
        }

        if (resultOfRequest.length() > 0){
            return resultOfRequest.toString();
        }

        Unit updatedUnit = unitRepository.findById(unit.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Единица измерения не найлдена"));

        updatedUnit = unit;
        unitRepository.save(updatedUnit);
        return "";
    }

    private Boolean validateInputData(Unit unit){
        return !(unit.getName() == null || unit.getName().isEmpty());
    }
}
