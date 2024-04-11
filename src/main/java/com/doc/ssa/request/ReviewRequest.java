package com.doc.ssa.request;

import lombok.Data;

@Data
public class ReviewRequest {
	
	private Integer consultationId;
	private Integer rating;
	private String feedback;

}
