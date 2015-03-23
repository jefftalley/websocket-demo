var map;
var flights = [];
var vans = [];

function initializeMap() {

    var myLatlng = new google.maps.LatLng(38.170331, -85.737431);
    var mapOptions = {
        zoom: 16,
        center: myLatlng
    };

    map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
}

function initializeSocket() {
	var socket = new SockJS('/requests');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		stompClient.subscribe('/topic/messages', function(message) {
			var messageBody = JSON.parse(message.body);
			if (messageBody.messageType === 'flightUpdate') {
				processFlight(messageBody.content);
			}
			if (messageBody.messageType === 'vanUpdate') {
				processVan(messageBody.content);
			}
		});
		stompClient.send("/app/requests", {}, 'init');
	});
}

function processFlight(flight) {

    var marker = new google.maps.Marker({
        position: new google.maps.LatLng(flight.latitude, flight.longitude),
        map: map,
        title: flight.flightNumber,
        icon: {
            scaledSize: new google.maps.Size(30, 30),
            url: 'http://png-5.findicons.com/files/icons/2219/dot_pictograms/128/air_plane_airport_2.png'
        }
    });

    var infoWindow = new google.maps.InfoWindow({
        content: '<b>Flight:</b> ' + flight.flightNumber + '<br><b>Route:</b> ' + flight.origin + '-' + flight.destination,
        maxWidth: 250
    });
    
    google.maps.event.addListener(marker, 'click', function () {
    	infoWindow.open(map, marker);
    });

    flight.marker = marker;
    flights.push(flight);
}

function processVan(van) {

	var matchingVan;
	
	vans.forEach(function(vanCheck) {
		if (vanCheck.vanId === van.vanId) {
			matchingVan = vanCheck;
		}
	});
	
	if (matchingVan) {
		matchingVan.latitude = van.latitude; 
		matchingVan.longitude = van.longitude; 
		matchingVan.marker.setPosition(new google.maps.LatLng(van.latitude, van.longitude));
	} else {
		var marker = new google.maps.Marker({
			position: new google.maps.LatLng(van.latitude, van.longitude),
			map: map,
			title: van.vanId,
			icon: {
				// scaledSize: new google.maps.Size(30, 30),
				url: 'images/minibus1.png'
			}
		});
		van.marker = marker;
		vans.push(van);
	}

//    var infoWindow = new google.maps.InfoWindow({
//        content: '<b>Flight:</b> ' + flight.flightNumber + '<br><b>Route:</b> ' + flight.origin + '-' + flight.destination,
//        maxWidth: 250
//    });
//    
//    google.maps.event.addListener(marker, 'click', function () {
//    	infoWindow.open(map, marker);
//    });
}

initializeMap();
initializeSocket();
