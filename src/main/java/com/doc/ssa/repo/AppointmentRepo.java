package com.doc.ssa.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doc.ssa.model.Appointment;

public interface AppointmentRepo extends JpaRepository<Appointment, Integer> {

	Optional<Appointment> findByAppointmentId(Integer appointmentId);
	
}
