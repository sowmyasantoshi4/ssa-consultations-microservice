package com.doc.ssa.controller;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.doc.ssa.dto.ConsultationDto;
import com.doc.ssa.model.Consultation;
import com.doc.ssa.repo.ConsultationRepo;
import com.doc.ssa.request.ConsultationRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class ConsultationController {
	
	@Autowired
	private ConsultationRepo conRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Operation(summary = "Add a consultation")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Consultation Added",
					  content = { @Content(mediaType = "application/json", 
				      schema = @Schema(implementation = ConsultationDto.class)) } ),
			  //@ApiResponse(responseCode = "404", description = "Consultation not added"),
			  //@ApiResponse(responseCode = "400", description = "Consultation not added")
			  })
	@PostMapping("/save")
    public ResponseEntity<ConsultationDto> save(@RequestBody ConsultationRequest req) throws Exception {
    	ResponseEntity<ConsultationDto> re = null;
    	Consultation cons = new Consultation();
    	ConsultationDto consDto = null;
    	Optional<Consultation> opt = null;
    	Integer appId = null;
    	
    	appId = req.getAppointmentId();
    	opt = conRepo.findByAppointmentId(appId);
    	
    	if( opt.isEmpty() ) {
    		cons.setAppointmentId(req.getAppointmentId());
    		cons.setPrescription(req.getPrescription());
    		cons = conRepo.save(cons);
    		consDto = modelMapper.map(cons, ConsultationDto.class);
    		re = new ResponseEntity<ConsultationDto>(consDto, HttpStatus.OK);
    	} else {
    		throw new Exception("Consultation already Added");
    	}
    	
        return re;
    }
    
	@Operation(summary = "Get list of Consultations")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Found the Consultations", 
			    content = { @Content(mediaType = "application/json", 
			      array = @ArraySchema(schema = @Schema(implementation = Consultation.class) ) ) } ),
			  @ApiResponse(responseCode = "404", description = "No Consultations found", 
			    content = @Content) })
    @GetMapping("/list")
    public ResponseEntity<List<Consultation>> list() {
    	ResponseEntity<List<Consultation>> re = null;
    	List<Consultation> cons = null;
    	
    	cons = conRepo.findAll();
    	//cons.stream().map( x-> consultationsDto.add(modelMapper.map(x, ConsultationDto.class))).collect(Collectors.toList());
    	
    	re = new ResponseEntity<List<Consultation>>(cons, HttpStatus.OK);
    	
        return re;
    }
	
	/*
	@Operation(summary = "Add a doctor profile")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Doctor profile Added", 
			    content = { @Content(mediaType = "application/json", 
			      schema = @Schema(implementation = Book.class)) }),
			  @ApiResponse(responseCode = "404", description = "Doctor profile not added", 
			    content = @Content) })
	@PostMapping("/addDoctor")
    public ResponseEntity<DoctorDto> addDoctor(@RequestBody DoctorRequest reqDoc) {
    	ResponseEntity<DoctorDto> re = null;
    	Doctor doctor = null;
    	DoctorDto doctorDto = null;
    	
    	doctor = new Doctor();
    	doctor.setDoctorName(reqDoc.getDoctorName());
    	doctor.setClinicId(reqDoc.getClinicId());
    	doctor.setSpecializationId(reqDoc.getSpecializationId());
    	doctor.setMobileNo(reqDoc.getMobileNo());
    	doctor.setHireDate(reqDoc.getHireDate());
    	doctor.setStatus(reqDoc.getStatus());
    	
    	doctor = docRepo.save(doctor);
    	doctorDto = modelMapper.map(doctor, DoctorDto.class);
    	
    	re = new ResponseEntity<DoctorDto>(doctorDto, HttpStatus.OK);
    	
        return re;
    }
	
	@Operation(summary = "Get list of Doctors")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "List of Doctors", 
			    content = { @Content(mediaType = "application/json", 
			      schema = @Schema(implementation = DoctorDto.class)) }),
			  @ApiResponse(responseCode = "404", description = "List of Doctors not found", 
			    content = @Content) })
    @GetMapping("/list")
    public ResponseEntity<List<DoctorDto>> listDoctors() {
    	ResponseEntity<List<DoctorDto>> re = null;
    	List<DoctorDto> docsDto = new ArrayList<>();
    	List<?> docs = null;
    	//docs = docRepo.findAll();
    	
    	docs = docRepo.allDoctors();
    	docs.stream().map( x-> docsDto.add(modelMapper.map(x, DoctorDto.class))).collect(Collectors.toList());
    	
    	re = new ResponseEntity<List<DoctorDto>>(docsDto, HttpStatus.OK);
    	
        return re;
    }
    */
    
}