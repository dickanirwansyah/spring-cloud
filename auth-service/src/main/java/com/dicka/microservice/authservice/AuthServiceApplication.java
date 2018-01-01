package com.dicka.microservice.authservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import java.io.Serializable;
import java.security.Principal;
import java.util.Optional;
import java.util.stream.Stream;

@SpringBootApplication
@EnableResourceServer
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}
}


@RestController
class PrincipalRestController{


	@RequestMapping("/user")
	Principal principal (Principal principal){
		return principal;
	}
}


@Configuration
@EnableAuthorizationServer
class OAuth2Configurations extends AuthorizationServerConfigurerAdapter{

	private final AuthenticationManager authenticationManager;

	@Autowired
	public OAuth2Configurations(AuthenticationManager authenticationManager){
		this.authenticationManager=authenticationManager;
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient("acme")
				.secret("acmesecret")
				.authorizedGrantTypes("password")
				.scopes("openid");
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(this.authenticationManager);
	}
}

@Component
class AccountCRL implements CommandLineRunner{


	@Override
	public void run(String... strings) throws Exception {
		Stream.of("dicka,spring", "jlong,spring", "sita,cloud")
				.map(x -> x.split(","))
				.forEach(tuple -> this.accountRepository
						.save(new Account(
								tuple[0], tuple[1] , true)));
	}

	private final AccountRepository accountRepository;

	@Autowired
	public AccountCRL(AccountRepository accountRepository){
		this.accountRepository = accountRepository;
	}
}

@Service
class AccountUserDetailsService implements UserDetailsService{

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return this.accountRepository.findByUsername(username)
				.map(account -> new User(
						account.getUsername(),
						account.getPassword(),
						account.isActive(),account.isActive(),account.isActive(),account.isActive(),
						AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER")
				))
				.orElseThrow(()->
						new UsernameNotFoundException("opps..sory not found username "+username+" !"));
	}

	private final AccountRepository accountRepository;

	@Autowired
	public AccountUserDetailsService(AccountRepository accountRepository){
		this.accountRepository = accountRepository;
	}
}


interface AccountRepository extends JpaRepository<Account, Long>{

	Optional<Account> findByUsername(String username);
}


@Entity
class Account implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String password;
	private boolean active;

	public Account(){
	}

	public Account( String username, String password, boolean active) {
		this.username = username;
		this.password = password;
		this.active = active;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}