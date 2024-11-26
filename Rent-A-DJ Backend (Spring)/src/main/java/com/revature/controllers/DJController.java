package com.revature.controllers;

import com.revature.daos.DJDAO;
import com.revature.models.DJ;
import com.revature.models.User;
import com.revature.services.DJService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/djs")
@CrossOrigin
public class DJController {

    private static final Logger logger = LoggerFactory.getLogger(DJController.class);

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

        logger.info("Successfully registered DJ with username: {}", newDJ.getUsername());
        return ResponseEntity.status(201).body(dj);
    }

    // This handles getting all DJs
    @GetMapping
    public ResponseEntity<List<DJ>> getAllDJ() {

        List<DJ> allDJs = djService.getAllDJs();

        logger.info("Retrieved {} DJs from the database", allDJs.size());
        return ResponseEntity.ok(allDJs);
    }

    //Changed this, dont know if it breaks something else tho
    // This handles rate change for the DJ
    @PatchMapping("/{djId}/rate")
    public ResponseEntity<DJ> changeRate(@PathVariable int djId,
                                         @RequestBody Map<String, Double> rate) {
        DJ dj = djService.changeRate(djId, rate.get("rate"));

        logger.info("Successfully updated rate for DJ with ID: {}", djId);
        return ResponseEntity.ok(dj);
    }

    //handles password changes for the DJ
    @PatchMapping("/{djId}/password")
    public ResponseEntity<DJ> changePassword(@PathVariable int djId, @RequestBody String password){
        DJ dj = djService.changePassword(djId, password);

        logger.info("Successfully changed password for DJ with ID: {}", djId);
        return ResponseEntity.ok(dj);
    }

    //handles username changes for the DJ
    @PatchMapping("/{djId}/username")
    public ResponseEntity<DJ> changeUsername(@PathVariable int djId, @RequestBody String username){
        DJ dj = djService.changeUsername(djId, username);

        logger.info("Successfully changed username for DJ with ID: {}", djId);
        return ResponseEntity.ok(dj);
    }

    // This handles removing a DJ from DB by username
    @DeleteMapping("/{username}")
    public ResponseEntity<DJ> removeDJ(@PathVariable String username) {
        DJ djToDelete = djService.findByUsername(username);

        if (djToDelete != null) {
            djService.removeDJ(djToDelete.getDjId());
            logger.info("Successfully removed DJ with username: {}", username); // Log if DJ was successfully removed
        } else {
            logger.warn("DJ with username: {} not found", username); // Log warning if DJ not found
        }

        return ResponseEntity.ok(djToDelete);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        logger.error("Handled IllegalArgumentException: {}", e.getMessage());

        return ResponseEntity.status(400).body(e.getMessage());
    }
}
