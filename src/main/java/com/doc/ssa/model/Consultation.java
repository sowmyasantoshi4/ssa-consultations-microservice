package com.doc.ssa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name="consultations")
@Data
public class Consultation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "consultation_id")
	private Integer consultationId;
	@Column(name = "appointment_id")
	private Integer appointmentId;
	@Column(name = "prescription")
	private String prescription;
	@Column(name = "feedback")
	private String feedback;
	@Column(name = "rating")
	private Integer rating;
	
}
