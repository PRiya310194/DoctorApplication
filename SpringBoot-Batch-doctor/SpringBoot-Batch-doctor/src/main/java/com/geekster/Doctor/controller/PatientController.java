package com.geekster.Doctor.controller;

import com.geekster.Doctor.dao.DoctorRepository;
import com.geekster.Doctor.model.Doctor;
import com.geekster.Doctor.model.Patient;
import com.geekster.Doctor.service.PatientService;
import jakarta.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
public class PatientController {

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    PatientService service;

    @PostMapping(value = "/patient")
    public String savePatient(@RequestBody String patientRequest) {

        JSONObject json = new JSONObject(patientRequest);
        Patient patient = setPatient(json);
        service.savePatient(patient);

        return "Saved patient";

    }

    private Patient setPatient(JSONObject json) {

        Patient patient = new Patient();

        patient.setPatientId(json.getInt("patientId"));
        patient.setPatientName(json.getString("patientName"));
        patient.setAge(json.getInt("age"));
        patient.setPhoneNumber(json.getString("phoneNumber"));
        patient.setDiseaseType(json.getString("diseaseType"));
        patient.setGender(json.getString("gender"));

        Timestamp currTime = new Timestamp(System.currentTimeMillis());
        patient.setAdmitDate(currTime);

        String doctorId = json.getString("doctorId");
        Doctor doctor = doctorRepository.findById(Integer.valueOf(doctorId)).get();
        patient.setDoctor(doctor);

        return patient;


    }


    @GetMapping(value = "/patient")
    public ResponseEntity getPatients(@Nullable @RequestParam String doctorId,
                                      @Nullable @RequestParam String patientId) {

        JSONArray patientDetails = service.getPatients();

        return new ResponseEntity<>(patientDetails.toString(), HttpStatus.OK);

        //both null- all patients
        //doctorId null- get by patient Id
        //patientId null- get all patients been treated by doctorId

    //ToDO: get all patients, Get a single patient by patient id, Get all patient who are been treated by doctorId


    }
}
