package com.sevenstore.controller;

import com.sevenstore.entity.Unit;
import com.sevenstore.repository.UnitRepository;
import com.sevenstore.security.CheckApiKey;
import com.sevenstore.service.UnitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    @CheckApiKey
    @GetMapping()
    public List<Unit> findUnits() {
        return unitRepository.findAll();
    }

    @CheckApiKey
    @PostMapping()
    public ResponseEntity<?> createUnit(@RequestBody Unit unit){
        String resultOfRequest = unitService.createUnit(unit);

        if (!resultOfRequest.isEmpty()){
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, resultOfRequest);
            return ResponseEntity.badRequest().body(problemDetail);
        }

        return ResponseEntity.ok().build();
    }

    @CheckApiKey
    @PatchMapping()
    public ResponseEntity<?> editUnit(@RequestBody Unit unit){
        String resultOfRequest = unitService.editUnit(unit);
        if (!resultOfRequest.isEmpty()) {
            ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, resultOfRequest);
            return ResponseEntity.badRequest().body(problem);
        }

        return ResponseEntity.ok().build();
    }
}
