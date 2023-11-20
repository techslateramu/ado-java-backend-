package com.eastvantage.appointment.service;

import static com.eastvantage.appointment.common.AppointmentCommon.*;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastvantage.appointment.dto.AppointmentDto;
import com.eastvantage.appointment.dto.AppointmentFilterDto;
import com.eastvantage.appointment.entity.Appointment;
import com.eastvantage.appointment.exception.AppointmentException;
import com.eastvantage.appointment.exception.AppointmentNotFoundException;
import com.eastvantage.appointment.repository.AppointmentRepository;
import com.eastvantage.appointment.response.AppointmentResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * * @author Sudharshan B S
 * 
 * This is the service implementation class of Appointment Service interface.
 * This will contain all the declared of the Appointment Service interface whose
 * methods are defined
 *
 */

@Service
@Slf4j
public class AppointmentServiceImplementation implements AppointmentService {

	@Autowired
	private AppointmentRepository appointmentRepository;

	/**
	 * This is the implementation method of creating an appointment. Here we will
	 * convert the received dto to the entity object and save.
	 * 
	 * @return This will return the appointment object on successful saving of the
	 *         object.
	 */

	@Override
	public Appointment createAppointment(AppointmentDto appointmentDto) {
		log.debug(DEBUG, appointmentDto);
		Appointment appointment = new Appointment();
		try {
			BeanUtils.copyProperties(appointmentDto, appointment);
			appointment.setAppointmentDuration(convertTimeToMilliSeconds(appointmentDto.getAppointmentDuration()));
			return appointmentRepository.save(appointment);
		} catch (Exception e) {
			log.error(ERROR, e);
			throw new AppointmentException(SOMETHING_WENT_WRONG);
		}
	}

	/**
	 * This is a private method which is used to convert string duration format to
	 * Duration type.
	 * 
	 * @return This will return the milliseconds of the converted duration object
	 */
	private long convertTimeToMilliSeconds(String time) {
		String[] values = time.split(":");
		// get the hours, minutes and seconds value and add it to the duration
		Duration duration = Duration.ofHours(Integer.parseInt(values[0]));
		duration = duration.plusMinutes(Integer.parseInt(values[1]));
		duration = duration.plusSeconds(Integer.parseInt(values[2]));
		return duration.toMillis();
	}

	/**
	 * This is the implementation method of updating an appointment. Here we will
	 * find the object to be updated and replace all the changes and save it
	 * 
	 * @return This will return the appointment object on successful updating of the
	 *         object.
	 */
	@Transactional
	@Override
	public Appointment updateAppointment(AppointmentDto appointmentDto, long appointmentId) {
		try {
			log.debug(DEBUG, appointmentDto + " " + appointmentId);
			Appointment appointment = getAppointmentById(appointmentId);

			BeanUtils.copyProperties(appointmentDto, appointment, "appointmentId");
			appointment.setAppointmentDuration(convertTimeToMilliSeconds(appointmentDto.getAppointmentDuration()));
			return appointmentRepository.save(appointment);
		} catch (AppointmentNotFoundException e) {
			throw e;
		} catch (Exception e) {
			log.error(ERROR, e);
			throw new AppointmentException(SOMETHING_WENT_WRONG);
		}
	}

	/**
	 * This is a private method for fetching an appointment which is not deleted
	 * based on the appointment id
	 * 
	 * @return This will return the appointment object if it is present
	 */
	private Appointment getAppointmentById(long appointmentId) {
		log.debug(DEBUG);
		Appointment appointment = appointmentRepository.findByAppointmentIdAndIsDelete(appointmentId, false);
		if (!Objects.isNull(appointment)) {
			return appointment;
		} else {
			throw new AppointmentNotFoundException(GET_FAIL_MESSAGE);
		}
	}

	/**
	 * This is the implementation method of deleting an appointment. This method
	 * will do a soft delete operation on a particular appointment object
	 * 
	 * @return This will return the appointment object on successful deletion of the
	 *         object.
	 */
	@Transactional
	@Override
	public Appointment deleteAppointment(long appointmentId) {
		try {
			log.debug(DEBUG, appointmentId);
			Appointment appointment = getAppointmentById(appointmentId);
			appointment.setDelete(true);
			return appointmentRepository.save(appointment);
		} catch (AppointmentNotFoundException e) {
			throw e;
		} catch (Exception e) {
			log.error(ERROR, e);
			throw new AppointmentException(SOMETHING_WENT_WRONG);
		}
	}

	/**
	 * This is the implementation method of fetching all the appointments. This
	 * method will filter the appointments based on the appointment date if the from
	 * date and to date is passed
	 * 
	 * @return This will return the list of appointment objects
	 */
	@Override
	public List<AppointmentResponse> getAllAppointment(AppointmentFilterDto dto) {
		try {
			log.debug(DEBUG, dto);
			List<Appointment> list;
			if (dto.getFromDate() != null && dto.getToDate() != null) {
				list = appointmentRepository.findByIsDeleteAndAppointmentDateTimeBetween(false, dto.getFromDate(),
						dto.getToDate());
			} else {
				list = appointmentRepository.findByIsDelete(false);
			}
			return list.stream()
					.map(e -> new AppointmentResponse(e.getAppointmentId(), e.getAppointmentDateTime(),
							e.getAppointmentName(),
							DurationFormatUtils.formatDuration(e.getAppointmentDuration(), "HH:mm:ss", true),
							e.isDelete()))
					.collect(Collectors.toList());
		} catch (Exception e) {
			log.error(ERROR, e);
			throw new AppointmentException(SOMETHING_WENT_WRONG);
		}
	}

	/**
	 * This is a implementation method for fetching a particular appointment based
	 * on the appointment id.
	 * 
	 * @return This will return the particular appointment object based on the
	 *         appointment id
	 */
	@Override
	public AppointmentResponse findAppointmentById(long appointmentId) {
		try {
			log.debug(DEBUG, appointmentId);
			Appointment appointment = getAppointmentById(appointmentId);
			AppointmentResponse appointmentResponse = new AppointmentResponse();
			BeanUtils.copyProperties(appointment, appointmentResponse);
			appointmentResponse.setAppointmentDuration(
					DurationFormatUtils.formatDuration(appointment.getAppointmentDuration(), "HH:mm:ss", true));
			return appointmentResponse;
		} catch (AppointmentNotFoundException e) {
			throw e;
		} catch (Exception e) {
			log.error(ERROR, e);
			throw new AppointmentException(SOMETHING_WENT_WRONG);
		}
	}

}
