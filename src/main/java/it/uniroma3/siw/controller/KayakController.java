package it.uniroma3.siw.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import it.uniroma3.siw.model.Link;
import it.uniroma3.siw.model.Pipeline;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.PipelineRepository;
import it.uniroma3.siw.repository.UtenteRepository;
import it.uniroma3.siw.service.PipelineService;
import it.uniroma3.siw.service.UtenteService;


@Controller
@SuppressWarnings("unused")
public class KayakController {

	private String propertiesConfigFilePath = "/application.properties";
	private LinkedList<String> addedInput = new LinkedList<String>();
	private LinkedList<Link> links = new LinkedList<Link>();
	private String isWatchpoint;
	@Autowired
	private PipelineRepository pipelineRepository;
	@Autowired
	private PipelineService pipelineService;
	@Autowired
	private UtenteRepository utenteRepository;
	@Autowired
	private UtenteService utenteService;

	private RestTemplate restTemplate = new RestTemplate();
	@SuppressWarnings("unchecked")
	private LinkedList<String> prList	 = (LinkedList<String>) restTemplate.getForObject("http://localhost:8080/rest/primitiveList", LinkedList.class);

	@GetMapping("/refresh")
	public String refresh() {

		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject("http://localhost:8080/rest/refresh", String.class);
		System.out.println(result + "  -----  > refresh ok");
		return "kayakHome";
	}

	@GetMapping("/list")
	public String list() {

		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject("http://localhost:8080/rest/list", String.class);
		System.out.println(result + "  ------- > list ok"); 
		return "kayakHome";
	}

	@GetMapping("/reset")
	public String reset() {

		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject("http://localhost:8080/rest/reset", String.class);
		System.out.println(result + "  ------- > reset ok"); 
		return "kayakHome";
	}


	@PostMapping("/insert")
	public String insert(@RequestParam("file") MultipartFile file , @RequestParam("header") String header,
			@RequestParam("categoryLabel") String categoryLabel, @RequestParam("separator") String separator, @RequestParam("description") String description) throws IOException {


		Properties prop = new Properties();
		InputStream input = KayakController.class.getResourceAsStream(propertiesConfigFilePath);
		prop.load(input);
		String csvPath = prop.getProperty("csv.path");

		System.out.println(csvPath);

		String fileName = csvPath + file.getOriginalFilename();

		System.out.println(fileName);

		Map<String, String> vars = new HashMap<String,String>();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		vars.put("fileName", fileName);
		System.out.println(fileName);

		vars.put("hasHeader",header);
		System.out.println(header);

		vars.put("separator", separator);
		System.out.println(separator);

		vars.put("categoryLabel", categoryLabel);
		System.out.println(categoryLabel);

		vars.put("description", description);

		String url = "http://localhost:8080/rest/insert?filePath={fileName}&hasHeader={hasHeader}&charSeparator={separator}&categoryLabel={categoryLabel}&description={description}";

		RestTemplate restTemplate = new RestTemplate();

		String result = restTemplate.postForObject(url,headers,String.class,vars);

		System.out.println(result);

		return "kayakHome";
	}

	@GetMapping("/goEditor")
	public String goEditor(ModelMap model) {

		model.addAttribute("prList", prList);

		return "editor";
	}

	@PostMapping("/addInput")
	public String addInput(ModelMap model, @RequestParam String filename) {

		addedInput.add(filename);
		model.addAttribute("addedInput", addedInput);
		model.addAttribute("message", "input added");				
		model.addAttribute("prList", prList);

		return "editor";
	}

	@PostMapping("/addStage")
	public String addStage(ModelMap model, @RequestParam String primitive, @RequestParam String sxItem, @RequestParam String dxItem) {

		model.addAttribute("prList", prList);
		model.addAttribute("addedInput", addedInput);
		
		Link link = new Link();
		link.setDxItem(dxItem);
		link.setSxItem(sxItem);
		link.setStage(primitive);

		links.add(link);

		model.addAttribute("message", "stage added");

		return "editor";
	}

	@PostMapping("/addWatchpoint")
	public String addWatchpoint(ModelMap model) {

		model.addAttribute("prList", prList);
		model.addAttribute("addedInput", addedInput);
		model.addAttribute("message", "watchpoint added");

		isWatchpoint= "true";

		return "editor";
	}

	@PostMapping("/createPipeline")
	public String createPipeline(ModelMap model, @RequestParam String name, @RequestParam String description) {

		Pipeline pipe = new Pipeline();
		pipe.setCreationDate(new Date());
		pipe.setName(name);
		pipe.setDescription(description);	
		for (Link l : links ) l.setPipeline(pipe);
		pipe.setLinks(links);

		Utente user = utenteRepository.findByUsername(getUtenteConnesso()).get(0);
		pipe.setUser(user);

		System.out.println(pipe.toString());
		pipelineService.add(pipe);

		model.addAttribute("prList", prList);
		model.addAttribute("addedInput", addedInput);
		model.addAttribute("pipe", pipe);
		model.addAttribute("message", "pipeline created and saved");

		ObjectMapper mapper = new ObjectMapper();

		try {

			// Convert object to JSON string and pretty print
			String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pipe);
			System.out.println(jsonInString);

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.links=null;
		links= new LinkedList<Link>();
		return "editor";
	}

	public String getUtenteConnesso() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		return name;
	}

	@GetMapping("/listUserPipes")
	public String listUserPipes(ModelMap model) {
		ArrayList<Pipeline> pipeList = new ArrayList<>();
		Utente user = utenteRepository.findByUsername(getUtenteConnesso()).get(0);
		pipeList = (ArrayList<Pipeline>) pipelineRepository.findByUser(user);
		model.addAttribute("pipeList", pipeList);
		return "kayakHome";
	}


}
