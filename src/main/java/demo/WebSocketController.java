package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import demo.domain.FlightUpdate;
import demo.service.FlightService;

@Controller
public class WebSocketController {

	@Autowired
	private SimpMessagingTemplate template;
	
	@Autowired
	private FlightService flightService;
	
    @MessageMapping("/requests")
    public void request() throws Exception {
    	template.setDefaultDestination("/topic/messages");
		for (FlightUpdate flightUpdate : flightService.getAllFlights()) {
			System.out.println(flightUpdate.getFlightNumber());
			template.convertAndSend(flightUpdate);
		}
    }
}
