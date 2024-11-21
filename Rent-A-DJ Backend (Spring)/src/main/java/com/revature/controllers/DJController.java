package com.revature.controllers;

import com.revature.daos.DJDAO;
import com.revature.models.DJ;
import com.revature.models.User;
import com.revature.services.DJService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/djs")
public class DJController {

    private DJService djService;
    private DJDAO djDAO;

    @Autowired
    public DJController(DJService djService, DJDAO djDAO) {
        this.djService = djService;
        this.djDAO = djDAO;
    }

    // This handles registering a new DJ
    @PostMapping
    public ResponseEntity<DJ> registerDJ(@RequestBody DJ newDJ) {
        DJ dj = djService.registerDJ(newDJ.getFirstName(), newDJ.getLastName(),
                newDJ.getUsername(), newDJ.getPassword(), newDJ.getRate());
        return ResponseEntity.status(201).body(dj);
    }

    // This handles getting all DJs
    @GetMapping
    public ResponseEntity<List<DJ>> getAllDJ() {

        List<DJ> allDJs = djService.getAllDJs();

        return ResponseEntity.ok(allDJs);
    }

    // This handles rate change for the DJ
    @PatchMapping("/{djId}/rate")
    public ResponseEntity<DJ> updateRate(@PathVariable int djId,
                                         @RequestBody double rate) {
        DJ dj = djService.changeRate(djId, rate);
        return ResponseEntity.ok(dj);
    }
}
