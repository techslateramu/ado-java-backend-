package com.eastvantage.appointment.controller;

import static com.eastvantage.appointment.common.AppointmentCommon.*;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eastvantage.appointment.dto.AppointmentDto;
import com.eastvantage.appointment.dto.AppointmentFilterDto;
import com.eastvantage.appointment.response.AppointmentResponse;
import com.eastvantage.appointment.response.SuccessResponse;
import com.eastvantage.appointment.service.AppointmentService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

/**
 * * @author Sudharshan B S
 * 
 * This is the controller class of the Appointment. It contains methods such as
 * create, update, delete, find and find all methods of the appointment
 * 
 */

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
@Slf4j
public class AppointmentController {

	@Autowired
	private AppointmentService appointmentService;

	/**
	 * This is a controller method of Appointment. It is used for creating/ saving
	 * the particular appointment details.
	 * 
	 * @param appointmentDto This is the appointment object which needs to be saved
	 * 
	 * @return This method returns a success response object which contains the
	 *         error, success message, data which is saved and HTTP status code on
	 *         successful saving of the appointment details
	 */
	@Operation(summary = "This API used for creating/ saving the particular appointment details")
	@PostMapping("/appointment")
	public ResponseEntity<SuccessResponse> createAppointment(@Valid @RequestBody AppointmentDto appointmentDto) {
		log.debug(DEBUG, appointmentDto);
		return new ResponseEntity<>(
				new SuccessResponse(false, SAVE_SUCCESS_MESSAGE, appointmentService.createAppointment(appointmentDto)),
				HttpStatus.OK);
	}

	/**
	 * This is a controller method of Appointment. It is used for updating the
	 * particular appointment details.
	 * 
	 * @param appointmentDto This is the appointment object which needs to be
	 *                       updated
	 * @param appointmentId  This is the appointment id of the received appointment
	 *                       object
	 * @return This method returns a success response object which contains the
	 *         error, success message, data which is saved and HTTP status code on
	 *         successful update of the appointment details
	 */
	@Operation(summary = "This API used for updating the particular appointment details")
	@PutMapping("/appointment")
	public ResponseEntity<SuccessResponse> updateAppointment(@Valid @RequestBody AppointmentDto appointmentDto,
			@RequestParam("appointmentId") long appointmentId) {
		log.debug(DEBUG, appointmentDto + " " + appointmentId);
		return new ResponseEntity<>(new SuccessResponse(false, UPDATE_SUCCESS_MESSAGE,
				appointmentService.updateAppointment(appointmentDto, appointmentId)), HttpStatus.OK);
	}

	/**
	 * This is a controller method of Appointment. It is used for deleting the
	 * particular appointment details.
	 * 
	 * @param appointmentId This is the appointment id of the object that needs to
	 *                      be deleted
	 * @return This method returns a success response object which contains the
	 *         error, success message, data which is saved and HTTP status code on
	 *         successful deletion of the appointment
	 */
	@Operation(summary = "This API used for deleting the particular appointment details")
	@DeleteMapping("/appointment")
	public ResponseEntity<SuccessResponse> deleteAppointment(@RequestParam("appointmentId") long appointmentId) {
		log.debug(DEBUG, appointmentId);
		return new ResponseEntity<>(
				new SuccessResponse(false, DELETE_SUCCESS_MESSAGE, appointmentService.deleteAppointment(appointmentId)),
				HttpStatus.OK);
	}

	/**
	 * This is a controller method of Appointment. It is used for fetching the
	 * particular appointment details.
	 * 
	 * @param appointmentId This is the appointment id of the object that needs to
	 *                      be fetched
	 * @return This method returns a success response object which contains the
	 *         error, success message, data which contains the respective
	 *         appointment object fetched based on the id and HTTP status code on
	 *         successful deletion of the appointment
	 */
	@Operation(summary = "This API used for fetching the particular appointment details")
	@GetMapping("/appointment")
	public ResponseEntity<SuccessResponse> findAppointmentById(@RequestParam("appointmentId") long appointmentId) {
		log.debug(DEBUG, appointmentId);
		return new ResponseEntity<>(
				new SuccessResponse(false, GET_SUCCESS_MESSAGE, appointmentService.findAppointmentById(appointmentId)),
				HttpStatus.OK);
	}

	/**
	 * * This is a controller method of Appointment. It is used for fetching all
	 * appointments based on date range
	 * 
	 * @param dto This is the object which contains the from date time and to date
	 *            time for filter
	 * @return This method returns a success response object which contains the
	 *         error, success message, list of appointment details based on the
	 *         given date range and HTTP status code on successful deletion of the
	 *         appointment. The status code will be 204 when there is no data found.
	 */
	@Operation(summary = "This API used for fetching all the appointment details based on date filter")
	@PostMapping("/appointment/filter")
	public ResponseEntity<SuccessResponse> getAllAppointment(@RequestBody AppointmentFilterDto dto) {
		log.debug(DEBUG, dto);
		List<AppointmentResponse> list = appointmentService.getAllAppointment(dto);
		if (!list.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, GET_SUCCESS_MESSAGE, list), HttpStatus.OK);
		} else
			return new ResponseEntity<>(new SuccessResponse(false, NO_DATA_FOUND, list), HttpStatus.NO_CONTENT);
	}
}
