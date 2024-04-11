package com.doc.ssa.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.doc.ssa.dto.AppointmentDto;
import com.doc.ssa.dto.ConsultationDto;
import com.doc.ssa.dto.ListConsultationDto;
import com.doc.ssa.exception.ErrorResponse;
import com.doc.ssa.exception.GlobalException;
import com.doc.ssa.model.Appointment;
import com.doc.ssa.model.Consultation;
import com.doc.ssa.repo.AppointmentRepo;
import com.doc.ssa.repo.ConsultationRepo;
import com.doc.ssa.request.AppointmentRequest;
import com.doc.ssa.request.ConsultationRequest;
import com.doc.ssa.request.FeedbackRequest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@OpenAPIDefinition(info=@Info(title="SSA - Consultation API"))
public class ConsultationController {
	
	@Autowired
	private ConsultationRepo conRepo;
	
	@Autowired
	private AppointmentRepo appRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Operation(summary = "Add a consultation")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Consultation Added",
					  content = { @Content(mediaType = "application/json", 
				      schema = @Schema(implementation = ConsultationDto.class)) } ),
			  @ApiResponse(responseCode = "404", description = "Consultation not added",
					  content = { @Content(mediaType = "application/json", 
				      schema = @Schema(implementation = ErrorResponse.class)) } ),
			  @ApiResponse(responseCode = "400", description = "Consultation not added",
					  content = { @Content(mediaType = "application/json", 
				      schema = @Schema(implementation = ErrorResponse.class)) } )
			  })
	@PostMapping("/save")
    public ResponseEntity<ConsultationDto> save(@RequestBody ConsultationRequest req) throws Exception {
    	ResponseEntity<ConsultationDto> re = null;
    	Consultation cons = new Consultation();
    	ConsultationDto consDto = null;
    	Optional<Consultation> copt = null;
    	//Optional<Appointment> aopt = null;
    	Integer appId = null;
    	
    	appId = req.getAppointmentId();
    	//aopt = appRepo.findByAppointmentId(appId);
    	
    	try {
    		//if( aopt.isPresent() ) {
    			copt = conRepo.findByAppointmentId(appId);
    			if( copt.isEmpty() ) {
    				cons.setAppointmentId(req.getAppointmentId());
    				cons.setPrescription(req.getPrescription());
    				cons = conRepo.save(cons);
    				consDto = modelMapper.map(cons, ConsultationDto.class);
    				re = new ResponseEntity<ConsultationDto>(consDto, HttpStatus.OK);
    			} else {
    				throw new GlobalException("Consultation already Added !");
    			}
//    		} else {
//    			throw new GlobalException("Invalid Appointment ID !");
//    		}
    	}catch (Exception e) {
			if(e.getMessage().contains("foreign key constraint fails")) {
				throw new GlobalException("Appointment ID doesnot exists !");
			}
		}
        return re;
    }
	
	@Operation(summary = "Add a feedback for consultation")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Feedback Added"),
			  @ApiResponse(responseCode = "404", description = "Feedback not added",
					  content = { @Content(mediaType = "application/json", 
				      schema = @Schema(implementation = ErrorResponse.class)) } ),
			  @ApiResponse(responseCode = "400", description = "Feedback not added",
					  content = { @Content(mediaType = "application/json", 
				      schema = @Schema(implementation = ErrorResponse.class)) } )
			  })
	@PostMapping("/feedback")
    public ResponseEntity<String> feedback(@RequestBody FeedbackRequest req) {
    	ResponseEntity<String> re = null;
    	Integer consId = null;
    	Consultation cons = new Consultation();
    	Optional<Consultation> opt = null;
    	
    	consId = req.getConsultationId();
    	
    	opt = conRepo.findById(consId);
    	
    	if( opt.isPresent() ) {
    		cons = opt.get();
    		if( null==cons.getRating() ) {
	    		cons.setRating(req.getRating());
	    		cons.setFeedback(req.getFeedback());
	    		conRepo.save(cons);
	    		re = new ResponseEntity<String>("Thanks for the Feedback !", HttpStatus.OK);
    		}else {
    			throw new GlobalException("Feedback already provided !");
    		}
    	}else {
    		throw new GlobalException("Invalid Consultation ID !");
    	}
    	
        return re;
    }
    
	@Operation(summary = "Get list of Consultations Done")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Found the Consultations", 
			    content = { @Content(mediaType = "application/json", 
			      array = @ArraySchema(schema = @Schema(implementation = ListConsultationDto.class) ) ) } ),
			  @ApiResponse(responseCode = "404", description = "No Consultations found", 
			    content = @Content) })
    @GetMapping("/list")
    public ResponseEntity<List<ListConsultationDto>> list() {
    	ResponseEntity<List<ListConsultationDto>> re = null;
    	List<ListConsultationDto> consDto = new ArrayList<>();
    	List<Map<String, String>> consHm = null;
    	
    	//consDto = conRepo.findAll();
    	
    	consHm = conRepo.findAllAppointmentConsultations();
    	consHm.stream()
    		.filter(x -> x.get("consultationId")!=null )
    		.map(x -> consDto.add(modelMapper.map(x, ListConsultationDto.class))).collect(Collectors.toList());
    	
    	re = new ResponseEntity<List<ListConsultationDto>>(consDto, HttpStatus.OK);
    	
        return re;
    }
	
	
	
	@Operation(summary = "Add a appointment ( to be removed once appointments microservice is ready to use ) ")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Appointment Added",
					  content = { @Content(mediaType = "application/json", 
				      schema = @Schema(implementation = AppointmentDto.class)) } ),
			  @ApiResponse(responseCode = "404", description = "Appointment not added",
					  content = { @Content(mediaType = "application/json", 
				      schema = @Schema(implementation = ErrorResponse.class)) } ),
			  @ApiResponse(responseCode = "400", description = "Appointment not added",
					  content = { @Content(mediaType = "application/json", 
				      schema = @Schema(implementation = ErrorResponse.class)) } )
			  })
	@PostMapping("/saveAppointment")
    public ResponseEntity<AppointmentDto> saveAppointment(@RequestBody AppointmentRequest req) throws Exception {
    	ResponseEntity<AppointmentDto> re = null;
    	Appointment appointment = new Appointment();
    	AppointmentDto appResp = new AppointmentDto();
    	
    	try {
    		 appointment = appRepo.save(modelMapper.map(req, Appointment.class));
    		 appResp = modelMapper.map(appointment, AppointmentDto.class);
    		re = new ResponseEntity<AppointmentDto>(appResp, HttpStatus.OK);
    	}catch (Exception e) {
			throw new GlobalException("Appointment not added. Error : "+e.getMessage());
		}
        return re;
    }
	
	@Operation(summary = "Get list of All Appointments with Consultations")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Found the Consultations", 
			    content = { @Content(mediaType = "application/json", 
			      array = @ArraySchema(schema = @Schema(implementation = ListConsultationDto.class) ) ) } ),
			  @ApiResponse(responseCode = "404", description = "No Consultations found", 
			    content = @Content) })
    @GetMapping("/listAppConsultations")
    public ResponseEntity<List<ListConsultationDto>> listConsultations() {
    	ResponseEntity<List<ListConsultationDto>> re = null;
    	List<ListConsultationDto> consDto = new ArrayList<>();
    	List<Map<String, String>> consHm = null;
    	try {
    		consHm = conRepo.findAllAppointmentConsultations();

    		consHm.stream().map(x -> consDto.add(modelMapper.map(x, ListConsultationDto.class))).collect(Collectors.toList());

    		re = new ResponseEntity<List<ListConsultationDto>>(consDto, HttpStatus.OK);
    	}catch (Exception e) {
				throw new GlobalException("Error : "+e.getMessage());
		}
    	
        return re;
    }
	
}