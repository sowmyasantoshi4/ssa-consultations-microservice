package com.doc.ssa.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ConsultationReviewDto {
	
	private Integer consultationId;
	private String feedback;
	private Integer rating;

}
