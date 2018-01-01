package com.dicka.microservice.reservationservice;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.stream.Stream;

@EnableBinding(ReservationChannel.class)
//enable binding
@EnableDiscoveryClient
//punya nya eureka : setiap app client wajib ditao di eureka @EnableDiscoveryClient !!
@SpringBootApplication
public class ReservationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationServiceApplication.class, args);

	}
}


@Component
class SampleDATACommanLineRunner implements CommandLineRunner{

	private final ReservationRepository reservationRepository;

	@Autowired
	public SampleDATACommanLineRunner(ReservationRepository reservationRepository){
		this.reservationRepository = reservationRepository;
	}

	@Override
	public void run(String... strings) throws Exception {
		Stream.of("Muhammad Dicka Nirwansyah", "Ibrahim Arif")
				.forEach(name -> reservationRepository.save(new Reservation(name)));
		reservationRepository.findAll()
				.forEach(System.out::println);
	}
}

@RepositoryRestResource
interface ReservationRepository extends JpaRepository<Reservation, Long>{

}

@Data
@ToString
@EqualsAndHashCode
@Entity
class Reservation implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String reservationName;

	public Reservation(){

	}

	public Reservation(String reservationName){
		this.reservationName = reservationName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReservationName() {
		return reservationName;
	}

	public void setReservationName(String reservationName) {
		this.reservationName = reservationName;
	}



}

