package com.doc.ssa.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ConsultationDto {
	
	private Integer consultationId;
	private Integer appointmentId;
	private String prescription;

}
