package com.doc.ssa.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ConsultationDto {
	
	private Integer appointmentId;
	private Integer consultationId;
	private String prescription;

}
