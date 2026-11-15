package com.sevenstore.controller;

import com.sevenstore.entity.Unit;
import com.sevenstore.repository.UnitRepository;
import com.sevenstore.service.UnitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/procurement/unit")
public class UnitController {

    private final UnitService unitService;
    private final UnitRepository unitRepository;

    public UnitController(UnitService unitService, UnitRepository unitRepository){
        this.unitService = unitService;
        this.unitRepository = unitRepository;
    }

    @GetMapping()
    public List<Unit> findUnits() {
        return unitRepository.findAll();
    }

    @PostMapping()
    public ResponseEntity<Void> createUnit(@RequestBody Unit unit){
        Unit createdUnit = unitService.createUnit(unit);
        return ResponseEntity.ok().build();
    }
}
