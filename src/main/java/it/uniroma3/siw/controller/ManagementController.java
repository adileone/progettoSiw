package it.uniroma3.siw.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Edge;
import it.uniroma3.siw.model.Pipeline;
import it.uniroma3.siw.model.Skill;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.model.Utente.Role;
import it.uniroma3.siw.repository.EdgeRepository;
import it.uniroma3.siw.repository.PipelineRepository;
import it.uniroma3.siw.repository.UtenteRepository;
import it.uniroma3.siw.service.UtenteService;

@Controller
public class ManagementController {

	@Autowired
	UtenteService utenteService;
	@Autowired
	UtenteRepository utenteRepository ;
	@Autowired
	EdgeRepository edgeRepository ;
	@Autowired
	PipelineRepository pipelineRepository ;


	@PostMapping("/addAdmin")
	public String addAdmin(@RequestParam String username, @RequestParam String email, 
			@RequestParam String password, ModelMap model) {


		if ( utenteRepository.findByUsername(username).isEmpty()) {

			Utente user = new Utente();
			user.setUsername(username);
			user.setPassword(new BCryptPasswordEncoder().encode(password));
			user.setEmail(email);
			user.setDataCreazione(new Date());
			user.setRole(Role.ROLE_ADMIN);
			utenteService.add(user);	

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

			model.addAttribute("succMessage","admin added");
			return "management";

		}

		else {

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

			model.addAttribute("errMessage","admin " + username + " already exists");
			return "management";

		}

	}


	@PostMapping("/addUser")
	public String addUser(@RequestParam String username, @RequestParam String email, 
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
			user.setEmail(email);
			user.setDataCreazione(new Date());
			user.setSkills(skills);
			user.setRole(Role.ROLE_USER);
			utenteService.add(user);

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

			model.addAttribute("succMessage","user added");
			return "management";

		}

		else {

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

			model.addAttribute("errMessage","user " + username + " already exists");
			return "management";

		}

	}

	@GetMapping("/removeUser")
	public String removeUser(@RequestParam Long id,Model model) {


		utenteRepository.deleteById(id);;

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


	@GetMapping("/watchPipe")
	public String modifyPipe(@RequestParam Long id,Model model) {

		ArrayList<Pipeline> pipeL = (ArrayList<Pipeline>) pipelineRepository.findAllById(id);
		Pipeline pipe = pipeL.get(0);
		ArrayList <Edge> linkList =(ArrayList<Edge>) edgeRepository.findByPipeline(pipe);
		model.addAttribute("pipe", pipe);
		model.addAttribute("linkList", linkList);
		
		ArrayList <String> linkListStamp = new ArrayList<String>();
		for (Edge e : linkList) linkListStamp.add("id : " + e.getId() + " items : " +  e.getSxItem().toString() + " ----> " + e.getDxItem().toString());

		model.addAttribute("linkListStamp", linkListStamp);
		
		return "modifyPipe";
	}

}
