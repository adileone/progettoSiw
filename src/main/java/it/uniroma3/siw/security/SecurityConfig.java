package it.uniroma3.siw.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.jdbcAuthentication().dataSource(dataSource)

		.passwordEncoder(new BCryptPasswordEncoder())
		.usersByUsernameQuery("SELECT username,password,1 FROM utente where username=?")
		.authoritiesByUsernameQuery("SELECT username,role FROM utente where username=?");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable()

		.authorizeRequests()
		.antMatchers("/","/signin","/images/**").permitAll()
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.loginPage("/login")
		.permitAll()
		.defaultSuccessUrl("/kayakHome", true)
		.and()
		.logout()
		.permitAll();

	}

}
