package com.dicka.microservice.reservationservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class ControllerTest {

    private final String message;

    @Autowired
    public ControllerTest(@Value("${message: from client}") String message){
        this.message = message;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/message")
    String read(){
        return this.message;
    }
}


@MessageEndpoint
class ReservationProcessor{

    @ServiceActivator(inputChannel = "input")
    public void onNewReservation(String reservationName){
        this.reservationRepository.save(new Reservation(reservationName));
    }

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationProcessor(ReservationRepository reservationRepository){
        this.reservationRepository=reservationRepository;
    }
}


interface ReservationChannel{

    @Input
    SubscribableChannel input();
}
