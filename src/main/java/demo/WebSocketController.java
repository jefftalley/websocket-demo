package demo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import demo.domain.FlightUpdate;
import demo.domain.Message;
import demo.domain.VanUpdate;
import demo.service.FlightService;

@Controller
public class WebSocketController {

	@Autowired
	private SimpMessagingTemplate template;
	
	@Autowired
	private FlightService flightService;

	@PostConstruct
	private void init() {
		template.setDefaultDestination("/topic/messages");
		setupSimulatedVans();
	}
	
    @MessageMapping("/requests")
    public void request() throws Exception {
		for (FlightUpdate flightUpdate : flightService.getAllFlights()) {
			sendFlightUpdate(flightUpdate);
		}
    }
    
    public void sendFlightUpdate(FlightUpdate flightUpdate) {
		template.convertAndSend(new Message("flightUpdate", flightUpdate));
    }

    public void sendVanUpdate(VanUpdate vanUpdate) {
    	template.convertAndSend(new Message("vanUpdate", vanUpdate));
    }
    
    private void setupSimulatedVans() {
    	try {
    		String vans = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("vans.txt"));
    		List<VanUpdate> vanUpdates = new ArrayList<VanUpdate>();
    		for (String van : vans.split("\r\n")) {
    			VanUpdate vanUpdate = new VanUpdate();
    			String[] items = van.split(" ");
    			vanUpdate.setVanId(items[0]);
    			vanUpdate.setLatitude(items[1]);
    			vanUpdate.setLongitude(items[2]);
    			vanUpdate.setDelay(Long.parseLong(items[3]) * 1000);
    			vanUpdates.add(vanUpdate);
    		}
    		
    		VanSimulation vanSimulation = new VanSimulation(vanUpdates);
    		Thread t = new Thread(vanSimulation);
    		t.start();
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    private class VanSimulation implements Runnable {
    	private List<VanUpdate> vanUpdates;
    	
    	public VanSimulation(List<VanUpdate> vanUpdates) {
    		this.vanUpdates = vanUpdates;
    	}
    	
		@Override
		public void run() {
			while (true) {
				for (VanUpdate vanUpdate : vanUpdates) {
					sendVanUpdate(vanUpdate);
					try {
						Thread.sleep(vanUpdate.getDelay());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
    	
    }
}
