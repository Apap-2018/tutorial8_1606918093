package com.apap.tutorial8.service;

import static org.junit.Assert.assertThat;

import java.sql.Date;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.apap.tutorial8.model.FlightModel;
import com.apap.tutorial8.repository.FlightDb;
import com.apap.tutorial8.service.FlightService;
import com.apap.tutorial8.service.FlightServiceImpl;

@RunWith(SpringRunner.class)
public class FlightServiceTest {
	@Autowired
	private static FlightService flightService;
	
	@MockBean
	private static FlightDb flightDb;
	
	@TestConfiguration
	static class CarServiceTestContextConfiguration{
		@Bean
		public FlightService flightService() {
			return new FlightServiceImpl();
		}
	}
		
	@Test
	public void whenValidFlightNumber_thenFlightShouldBeFound() {
		//given
		FlightModel flightModel = new FlightModel();
		flightModel.setFlightNumber("X550");
		flightModel.setOrigin("Depok");
		flightModel.setDestination("Bekasi");
		flightModel.setTime(new Date(new java.util.Date().getTime()));
		Optional<FlightModel> flight = Optional.of(flightModel);
		Mockito.when(flightDb.findByFlightNumber(flight.get().getFlightNumber())).thenReturn(flight);
		
		//when
		Optional<FlightModel> found = flightService.getFlightDetailByFlightNumber(flight.get().getFlightNumber());
			
		//then
		assertThat(found, Matchers.notNullValue());
		assertThat(found.get().getFlightNumber(), Matchers.equalTo(flightModel.getFlightNumber()));
	}
	
}
