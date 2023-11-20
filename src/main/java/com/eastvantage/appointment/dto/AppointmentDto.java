package com.eastvantage.appointment.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {

	@FutureOrPresent(message = "Provide A Valid Future Date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime appointmentDateTime;

	private String appointmentName;

	@Pattern(regexp = "(?:[01]\\d|2[0123]):(?:[012345]\\d):(?:[012345]\\d)", message = "Provide duration in the format of HH:MM:SS")
	private String appointmentDuration;

}
