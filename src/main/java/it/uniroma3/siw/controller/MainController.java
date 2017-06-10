package it.uniroma3.siw.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.model.Utente.Role;
import it.uniroma3.siw.service.UtenteService;

//controller to access the login page
@Controller
public class MainController {

	@Autowired
	UtenteService utenteService;

	// Login form
	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	// Login form with error
	@RequestMapping("/login-error.html")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		return "login";
	}

	@GetMapping("/kayakHome")
	public String goKayak() {
		return "kayakHome";
	}

	@GetMapping("/signin")
	public String getSigninPage() {
		return "signin";
	}
	
	
	@GetMapping("/userPage")
	public String userPage() {
		return "userPage";
	}

	@GetMapping("/management")
	public String management() {
		return "management";
	}
	

	@PostMapping("/signin")
	public String signUser(@RequestParam String username, @RequestParam String email, 
			@RequestParam String password, @RequestParam String repassword) {

		Utente user = new Utente();
		user.setUsername(username);
		user.setPassword(new BCryptPasswordEncoder().encode(password));
		user.setEmail(email);
		user.setDataCreazione(new Date());
		user.setRole(Role.ROLE_USER);
		utenteService.add(user);		

		return "signin";
	}

}