package com.eastvantage.appointment.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eastvantage.appointment.entity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	Appointment findByAppointmentIdAndIsDelete(long appointmentId, boolean b);

	List<Appointment> findByIsDelete(boolean b);

	List<Appointment> findByIsDeleteAndAppointmentDateTimeBetween(boolean b, LocalDateTime fromDate,
			LocalDateTime toDate);

}
