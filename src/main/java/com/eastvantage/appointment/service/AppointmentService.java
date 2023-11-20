package com.eastvantage.appointment.service;

import java.util.List;

import com.eastvantage.appointment.dto.AppointmentDto;
import com.eastvantage.appointment.dto.AppointmentFilterDto;
import com.eastvantage.appointment.entity.Appointment;
import com.eastvantage.appointment.response.AppointmentResponse;

public interface AppointmentService {

	public Appointment createAppointment(AppointmentDto appointmentDto);

	public Appointment updateAppointment(AppointmentDto appointmentDto, long appointmentId);

	public Appointment deleteAppointment(long appointmentId);

	public List<AppointmentResponse> getAllAppointment(AppointmentFilterDto dto);

	public AppointmentResponse findAppointmentById(long appointmentId);
}
