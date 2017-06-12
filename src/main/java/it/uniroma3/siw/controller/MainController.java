package it.uniroma3.siw.controller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.model.Skill;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.model.Utente.Role;
import it.uniroma3.siw.repository.UtenteRepository;
import it.uniroma3.siw.service.UtenteService;

//controller to access the login page
@Controller
public class MainController {

	@Autowired
	UtenteService utenteService;
	@Autowired
	UtenteRepository utenteRepository ;

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
	public String userPage(ModelMap model, Authentication authentication) {

		String name = authentication.getName();
		Utente user = utenteRepository.findByUsername(name).get(0);
		model.addAttribute("user",user);

		return "userPage";
	}

	@GetMapping("/management")
	public String management() {
		return "management";
	}


	@PostMapping("/signin")
	public String signUser(@RequestParam String username, @RequestParam String email, 
			@RequestParam String password, @RequestParam(required=false) String skill1, @RequestParam(required=false) String value1, 
			@RequestParam(required=false) String skill2, @RequestParam(required=false) String value2, 
			@RequestParam(required=false) String skill3, @RequestParam(required=false) String value3, final RedirectAttributes redirectAttributes) {

		List<Skill> skills = new LinkedList<Skill>();		

		if(!skill1.equals("")) {
			Skill skillOne = new Skill();
			skillOne.setName(skill1);
			skillOne.setValue(Double.parseDouble(value1));

			skills.add(skillOne);
		}

		if(!skill2.equals("")) {

			Skill skillTwo = new Skill();
			skillTwo.setName(skill2);
			skillTwo.setValue(Double.parseDouble(value2));

			skills.add(skillTwo);
		}

		if(!skill3.equals("")) {


			Skill skillThree = new Skill();
			skillThree.setName(skill3);
			skillThree.setValue(Double.parseDouble(value3));

			skills.add(skillThree);
		}



		if ( utenteRepository.findByUsername(username).isEmpty()) {

			Utente user = new Utente();
			user.setUsername(username);
			user.setPassword(new BCryptPasswordEncoder().encode(password));
			user.setEmail(email);
			user.setDataCreazione(new Date());
			user.setSkills(skills);
			user.setRole(Role.ROLE_USER);
			utenteService.add(user);	

			redirectAttributes.addFlashAttribute("message","you have been registered, please login");
			return "redirect:login";

		}

		else {

			redirectAttributes.addFlashAttribute("message","username already exists");
			return "redirect:signin";

		}


	}

	@GetMapping(value="/logout")
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){    
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}

}