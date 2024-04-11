package com.doc.ssa.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doc.ssa.model.Consultation;

public interface ConsultationRepo extends JpaRepository<Consultation, Integer> {

	Optional<Consultation> findByAppointmentId(Integer appointmentId);
}
