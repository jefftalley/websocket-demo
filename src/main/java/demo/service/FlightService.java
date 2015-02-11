package demo.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import demo.domain.Crew;
import demo.domain.FlightUpdate;

@Service
public class FlightService {
	public List<FlightUpdate> getAllFlights() {
		List<FlightUpdate> flightUpdates = new ArrayList<FlightUpdate>();
		
		FlightUpdate flightUpdate = new FlightUpdate();
		flightUpdate.setScheduleDepartureTime(DateTime.now());
		flightUpdate.setFlightNumber("UPS1234");
		flightUpdate.setTailNumber("N1234UP");
		flightUpdate.setOrigin("SDF");
		flightUpdate.setDestination("LAX");
		flightUpdate.setParkingPosition("S4");
		flightUpdate.setLatitude("38°10'5.30\"N");
		flightUpdate.setLongitude("85°44'12.47\"W");
		
		Crew crew = new Crew();
		crew.setGemsId("0555111");
		crew.setFirstName("John");
		crew.setLastName("Doe");
		crew.setPosition("CPT");
		crew.setStatus("onboard");

		flightUpdate.getCrews().add(crew);
		
		flightUpdates.add(flightUpdate);
		
		return flightUpdates;
	}
}
