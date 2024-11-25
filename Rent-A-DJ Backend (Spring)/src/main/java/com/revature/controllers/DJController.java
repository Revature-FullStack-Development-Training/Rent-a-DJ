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
@CrossOrigin
public class DJController {

    private DJService djService;
    //private DJDAO djDAO;

    @Autowired
    public DJController(DJService djService){ //, DJDAO djDAO) {
        this.djService = djService;
        //this.djDAO = djDAO;
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

    //Changed this, dont know if it breaks something else tho
    // This handles rate change for the DJ
    @PatchMapping("/{djId}/rate")
    public ResponseEntity<DJ> changeRate(@PathVariable int djId,
                                         @RequestBody Map<String, Double> rate) {
        DJ dj = djService.changeRate(djId, rate.get("rate"));
        return ResponseEntity.ok(dj);
    }

    // This handles removing a DJ from DB by username
    @DeleteMapping("/{username}")
    public ResponseEntity<DJ> removeDJ(@PathVariable String username) {
        DJ djToDelete = djService.findByUsername(username);

        djService.removeDJ(djToDelete.getDjId());
        return ResponseEntity.ok(djToDelete);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }
}
