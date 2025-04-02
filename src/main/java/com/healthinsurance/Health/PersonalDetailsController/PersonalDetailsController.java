package com.healthinsurance.Health.PersonalDetailsController;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthinsurance.Health.PersonalDetailsService.PersonalDetailsService;
import com.healthinsurance.Health.PersonalEntities.PersonalDetails;

@RestController
@RequestMapping("/api/personalDetails")
public class PersonalDetailsController {
	@Autowired
    private PersonalDetailsService personalDetailsService;

    @PostMapping
    public ResponseEntity<PersonalDetails> createPersonalDetails(@RequestBody PersonalDetails personalDetails) {
        PersonalDetails createdPersonalDetails = personalDetailsService.createPersonalDetails(personalDetails);
        return new ResponseEntity<>(createdPersonalDetails, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PersonalDetails>> getAllPersonalDetails() {
        List<PersonalDetails> personalDetailsList = personalDetailsService.getAllPersonalDetails();
        return new ResponseEntity<>(personalDetailsList, HttpStatus.OK);
    }

    @GetMapping("/{pid}")
    public ResponseEntity<PersonalDetails> getPersonalDetailsById(@PathVariable int pid) {
        Optional<PersonalDetails> personalDetails = personalDetailsService.getPersonalDetailsById(pid);
        return personalDetails.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{pid}")
    public ResponseEntity<PersonalDetails> updatePersonalDetails(@PathVariable int pid, @RequestBody PersonalDetails personalDetails) {
        personalDetails.setPid(pid);
        PersonalDetails updatedPersonalDetails = personalDetailsService.updatePersonalDetails(personalDetails);
        return new ResponseEntity<>(updatedPersonalDetails, HttpStatus.OK);
    }

    @DeleteMapping("/{pid}")
    public ResponseEntity<Void> deletePersonalDetails(@PathVariable int pid) {
        personalDetailsService.deletePersonalDetails(pid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
