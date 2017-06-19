package it.uniroma3.siw.controller;

import java.util.ArrayList;
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
import org.springframework.web.client.RestTemplate;

import it.uniroma3.siw.model.Edge;
import it.uniroma3.siw.model.Pipeline;
import it.uniroma3.siw.model.Skill;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.model.Utente.Role;
import it.uniroma3.siw.repository.EdgeRepository;
import it.uniroma3.siw.repository.PipelineRepository;
import it.uniroma3.siw.repository.UtenteRepository;
import it.uniroma3.siw.service.UtenteService;

//controller to access the login page
@Controller
public class MainController {

	@Autowired
	UtenteService utenteService;
	@Autowired
	UtenteRepository utenteRepository ;
	@Autowired
	EdgeRepository edgeRepository ;
	@Autowired
	PipelineRepository pipelineRepository ;

	private RestTemplate restTemplate = new RestTemplate();
	@SuppressWarnings("unchecked")
	private LinkedList<String> prList	 = (LinkedList<String>) restTemplate.getForObject("http://kayakmockbackend.eu-west-2.elasticbeanstalk.com/rest/primitiveList", LinkedList.class);

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
	public String goKayak(ModelMap model) {
		
		Utente user = new Utente();
		
		try {
			 user = utenteRepository.findByUsername(getUtenteConnesso()).get(0);
			
			}catch (Exception e) {			
				user.setUsername("admin");
				user.setDataCreazione(new Date());
				user.setEmail("default admin");
				user.setRole(Role.ROLE_ADMIN);
				utenteService.add(user);
			}
		

		try {

			ArrayList<Pipeline> pipeList = new ArrayList<>();
			user = utenteRepository.findByUsername(getUtenteConnesso()).get(0);
			pipeList = (ArrayList<Pipeline>) pipelineRepository.findByUser(user);
			model.addAttribute("pipeList", pipeList);	

		} catch (Exception e) {

			e.getMessage().toString();
		}

		return "kayakHome";
	}

	@GetMapping("/signin")
	public String getSigninPage() {
		return "signin";
	}

	@GetMapping("/editor")
	public String editor() {
		return "editor";
	}

	@GetMapping("/modifyPipe")
	public String modifyPipe(@RequestParam Long id,Model model) {

		ArrayList<Pipeline> pipeL = (ArrayList<Pipeline>) pipelineRepository.findAllById(id);
		Pipeline pipe = pipeL.get(0);
		ArrayList <Edge> linkList =(ArrayList<Edge>) edgeRepository.findByPipeline(pipe);
		model.addAttribute("pipe", pipe);
		model.addAttribute("linkList", linkList);

		ArrayList <String> linkListStamp = new ArrayList<String>();
		for (Edge e : linkList) linkListStamp.add("id : " + e.getId() + " items : " +  e.getSxItem().toString() + " ----> " + e.getDxItem().toString());

		model.addAttribute("prList", prList);
		model.addAttribute("linkListStamp", linkListStamp);

		return "modifyPipe";
	}

	@GetMapping("/removePipe")
	public String removePipe(@RequestParam Long id,Model model) {

		ArrayList<Pipeline> pipeL = (ArrayList<Pipeline>) pipelineRepository.findAllById(id);
		Pipeline pipe = pipeL.get(0);
		pipelineRepository.delete(pipe);

		try {

			ArrayList<Pipeline> pipeList = new ArrayList<>();
			Utente user = utenteRepository.findByUsername(getUtenteConnesso()).get(0);
			pipeList = (ArrayList<Pipeline>) pipelineRepository.findByUser(user);
			model.addAttribute("pipeList", pipeList);	

		} catch (Exception e) {

			e.getMessage().toString();
		}

		return "kayakHome";
	}

	@GetMapping("/userPage")
	public String userPage(ModelMap model, Authentication authentication) {

		String name = authentication.getName();
		Utente user = utenteRepository.findByUsername(name).get(0);
		model.addAttribute("user",user);

		return "userPage";
	}

	@GetMapping("/management")
	public String management(ModelMap model) {

		try {

			ArrayList<Utente> userList = new ArrayList<Utente>();
			userList = (ArrayList<Utente>) utenteRepository.findAll();
			model.addAttribute("userList", userList);	

		} catch (Exception e) {

			e.getMessage().toString();
		}


		try {

			ArrayList<Pipeline> kayakPList = new ArrayList<Pipeline>();
			kayakPList = (ArrayList<Pipeline>) pipelineRepository.findAll();
			model.addAttribute("kayakPList", kayakPList);	

		} catch (Exception e) {

			e.getMessage().toString();
		}


		return "management";
	}


	@PostMapping("/signin")
	public String signUser(@RequestParam String username, @RequestParam String email, 
			@RequestParam String password, @RequestParam(required=false) String skill1, @RequestParam(required=false) String value1, 
			@RequestParam(required=false) String skill2, @RequestParam(required=false) String value2, 
			@RequestParam(required=false) String skill3, @RequestParam(required=false) String value3, ModelMap model) {

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

			if (utenteRepository.findByEmail(email).isEmpty()) {

				user.setEmail(email);
				user.setDataCreazione(new Date());
				user.setSkills(skills);
				user.setRole(Role.ROLE_USER);
				utenteService.add(user);	

				model.addAttribute("message","you have been registered, please login");
				return "login";
			}
			else { user = null;
			model.addAttribute("message", "email already inserted, please choose other");
			return "signin";}

		}

		else {

			model.addAttribute("message","username already exists");
			return "signin";

		}


	}

	@PostMapping("updateSkills") 
	public String updateSkills (@RequestParam String skill1, @RequestParam String value1, @RequestParam String id, ModelMap model) {


		Utente user = utenteRepository.findByUsername(getUtenteConnesso()).get(0);

		Skill s = new Skill();
		s.setName(skill1);
		s.setValue(Double.parseDouble(value1));
		s.setUser(user);

		List<Skill> skill = user.getSkills();
		skill.add(s);

		user.setSkills(skill);

		utenteService.add(user);

		model.addAttribute("user",user);

		return "userPage";

	}

	@GetMapping(value="/logout")
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){    
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}

	public String getUtenteConnesso() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		return name;
	}

}