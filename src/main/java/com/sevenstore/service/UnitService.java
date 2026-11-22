package com.sevenstore.service;

import com.sevenstore.entity.Unit;
import com.sevenstore.repository.UnitRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.StringJoiner;

@Service
public class UnitService {

    private final UnitRepository unitRepository;

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    @Transactional
    public String createUnit(@RequestBody Unit unit) {

        StringJoiner resultOfRequest = new StringJoiner("; ");
        if (!validateInputData(unit)) {
           resultOfRequest.add("Название не заполнено");
        }

        if (resultOfRequest.length() > 0) {
            return resultOfRequest.toString();
        }

        unit.setCode(String.format("%03d",unitRepository.count()+1));

        unitRepository.save(unit);

        return "";
    }

    @Transactional
    public String editUnit(@RequestBody Unit unit) {

        StringJoiner resultOfValidation = new StringJoiner("; ");

        if (unit.getId() == null) {
            resultOfValidation.add("ID не указано");
            return resultOfValidation.toString();
        }

        Unit modifiedUnit = unitRepository.findById(unit.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Единица измерения не найлдена"));

        if (unit.getName() != null) {
            if (unit.getName().isEmpty()) {
                resultOfValidation.add("Название не заполнено");
            }
            else {
                modifiedUnit.setName(unit.getName());
            }
        }

        if (unit.getCode() != null) {
            if (unit.getCode().isEmpty()) {
                resultOfValidation.add("Код не заполнен");
            }
            else {
                modifiedUnit.setCode(unit.getCode());
            }
        }

        if (resultOfValidation.length() > 0) {
            return resultOfValidation.toString();
        }

        unitRepository.save(modifiedUnit);
        return "";
    }

    private Boolean validateInputData(Unit unit) {
        return !(unit.getName() == null || unit.getName().isEmpty());
    }
}
