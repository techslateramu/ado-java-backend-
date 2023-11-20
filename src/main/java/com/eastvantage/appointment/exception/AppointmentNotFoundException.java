package com.eastvantage.appointment.exception;

@SuppressWarnings("serial")
public class AppointmentNotFoundException extends RuntimeException {
	public AppointmentNotFoundException(String message) {
		super(message);
	}
}
