package it.uniroma3.siw.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;

import org.bson.Document;
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

import it.uniroma3.siw.model.Edge;
import it.uniroma3.siw.model.InputNode;
import it.uniroma3.siw.model.Node;
import it.uniroma3.siw.model.Pipeline;
import it.uniroma3.siw.model.StageNode;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.model.Utente.Role;
import it.uniroma3.siw.repository.PipelineRepository;
import it.uniroma3.siw.repository.UtenteRepository;
import it.uniroma3.siw.service.PipelineService;
import it.uniroma3.siw.service.UtenteService;

@Controller
@SuppressWarnings("unused")
public class KayakController {

	private String propertiesConfigFilePath = "/application.properties";
	private HashMap<Object,String> addedInput = new HashMap<Object,String>();
	private LinkedList<Edge> edges = new LinkedList<Edge>();
	private HashMap<String,Node> nodes = new HashMap<String,Node>();
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
	private LinkedList<String> prList	 = (LinkedList<String>) restTemplate.getForObject("http://kayakmockbackend.eu-west-2.elasticbeanstalk.com/rest/primitiveList", LinkedList.class);

	@GetMapping("/refresh")
	public String refresh(ModelMap model) {

		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject("http://kayakmockbackend.eu-west-2.elasticbeanstalk.com/rest/refresh", String.class);
		System.out.println(result + "  -----  > refresh ok");
		model.addAttribute("resultRefresh",result);
		
		try {

			Utente user = utenteRepository.findByUsername(getUtenteConnesso()).get(0);
			ArrayList<Pipeline> pipeList = new ArrayList<>();
			pipeList = (ArrayList<Pipeline>) pipelineRepository.findByUser(user);
			model.addAttribute("pipeList", pipeList);	

		} catch (Exception e) {

			e.getMessage().toString();
		}
		return "kayakHome";
	}

	@GetMapping("/list")
	public String list(ModelMap model) {

		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject("http://kayakmockbackend.eu-west-2.elasticbeanstalk.com/rest/list", String.class);
		System.out.println(result + "  ------- > list ok"); 
		model.addAttribute("resultList",result);
		
		try {

			Utente user = utenteRepository.findByUsername(getUtenteConnesso()).get(0);
			ArrayList<Pipeline> pipeList = new ArrayList<>();
			pipeList = (ArrayList<Pipeline>) pipelineRepository.findByUser(user);
			model.addAttribute("pipeList", pipeList);	

		} catch (Exception e) {

			e.getMessage().toString();
		}
		
		return "kayakHome";
	}

	@GetMapping("/reset")
	public String reset(ModelMap model) {

		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject("http://kayakmockbackend.eu-west-2.elasticbeanstalk.com/rest/reset", String.class);
		System.out.println(result + "  ------- > reset ok"); 
		model.addAttribute("resultReset",result);
		
		try {

			Utente user = utenteRepository.findByUsername(getUtenteConnesso()).get(0);
			ArrayList<Pipeline> pipeList = new ArrayList<>();
			pipeList = (ArrayList<Pipeline>) pipelineRepository.findByUser(user);
			model.addAttribute("pipeList", pipeList);	

		} catch (Exception e) {

			e.getMessage().toString();
		}
		
		return "kayakHome";
	}


	@PostMapping("/insert")
	public String insert(ModelMap model, @RequestParam("file") MultipartFile file , @RequestParam("header") String header,
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

		String url = "http://kayakmockbackend.eu-west-2.elasticbeanstalk.com/rest/insert?filePath={fileName}&hasHeader={hasHeader}&charSeparator={separator}&categoryLabel={categoryLabel}&description={description}";

		RestTemplate restTemplate = new RestTemplate();

		String result = restTemplate.postForObject(url,headers,String.class,vars);

		System.out.println(result);
		
		model.addAttribute("resultInsert",result);
		
		try {

			Utente user = utenteRepository.findByUsername(getUtenteConnesso()).get(0);
			ArrayList<Pipeline> pipeList = new ArrayList<>();
			pipeList = (ArrayList<Pipeline>) pipelineRepository.findByUser(user);
			model.addAttribute("pipeList", pipeList);	

		} catch (Exception e) {

			e.getMessage().toString();
		}

		return "kayakHome";
	}

	@GetMapping("/goEditor")
	public String goEditor(ModelMap model) {

		model.addAttribute("prList", prList);

		return "editor";
	}

	@PostMapping("/addInput")
	public String addInput(ModelMap model, @RequestParam String filename) {

		InputNode node = new InputNode();
		node.setFileName(filename);

		nodes.put(filename,node);

		addedInput.put(filename,"input");
		model.addAttribute("addedInput", addedInput);
		model.addAttribute("message", "input added");				
		model.addAttribute("prList", prList);

		return "editor";
	}

	@PostMapping("/addStage")
	public String addStage(ModelMap model, @RequestParam String stage) {

		StageNode node = new StageNode();
		node.setStageType(stage);

		nodes.put(stage,node);

		addedInput.put(stage,"stage");
		model.addAttribute("addedInput", addedInput);
		model.addAttribute("message", "stage added");				
		model.addAttribute("prList", prList);

		return "editor";
	}

	@PostMapping("/createLink")
	public String createLink(ModelMap model, @RequestParam String sxItem, @RequestParam String dxItem) {

		model.addAttribute("prList", prList);
		model.addAttribute("addedInput", addedInput);

		Edge edge = new Edge();

		edge.setDxItem(nodes.get(dxItem));
		edge.setSxItem(nodes.get(sxItem));	

		edges.add(edge);

		model.addAttribute("message", "link created");

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

	@GetMapping("/submitPipeline")
	public String submitPipeline(ModelMap model, @RequestParam Long id) {
		
		ArrayList<Pipeline> pipeL = (ArrayList<Pipeline>) pipelineRepository.findAllById(Long.valueOf(id));
		Pipeline pipe = pipeL.get(0);
		
		Utente user = utenteRepository.findByUsername(getUtenteConnesso()).get(0);
		pipe.setUser(user);
		
		ObjectMapper mapper = new ObjectMapper();

		try {

			// Convert object to JSON string and pretty print
			String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pipe);
			System.out.println(jsonInString);

			Document doc = Document.parse(jsonInString);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			Map<String, String> vars = new HashMap<String,String>();
			vars.put("doc", jsonInString);

			RestTemplate restTemplate = new RestTemplate();
			String url = "http://kayakmockbackend.eu-west-2.elasticbeanstalk.com/rest/getPipeline?doc={doc}";
			Document result = restTemplate.postForObject(url,headers,Document.class,vars);
			
			System.out.println(result);
			model.addAttribute("message",result);


		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			
			ArrayList<Pipeline> pipeList = new ArrayList<>();
			pipeList = (ArrayList<Pipeline>) pipelineRepository.findByUser(user);
			model.addAttribute("pipeList", pipeList);	

		} catch (Exception e) {

			e.getMessage().toString();
		}
		
		try {

			ArrayList<Pipeline> pipeList = new ArrayList<>();
			pipeList = (ArrayList<Pipeline>) pipelineRepository.findByUser(user);
			model.addAttribute("pipeList", pipeList);	

		} catch (Exception e) {

			e.getMessage().toString();
		}
		
		
		return "kayakHome";
	}
	
	@PostMapping("/createPipeline")
	public String createPipeline(ModelMap model, @RequestParam String name, @RequestParam String description) {

	
		
		Pipeline pipe = new Pipeline();
		pipe.setCreationDate(new Date());
		pipe.setName(name);
		pipe.setDescription(description);	

		LinkedList<Node> nodi = new LinkedList<Node>();

		for (Edge e : edges ) {
			e.setPipeline(pipe);
		}

		for (String s : nodes.keySet())  {
			Node n = nodes.get(s);
			nodi.add(n);
			n.setPipeline(pipe);
		}


		pipe.setNodes(nodi);
		pipe.setEdges(edges);

		
		Utente user = utenteRepository.findByUsername(getUtenteConnesso()).get(0);
		pipe.setUser(user);
		

		System.out.println(pipe.toString());
		pipelineService.add(pipe);

		model.addAttribute("prList", prList);
		model.addAttribute("addedInput", addedInput);
		model.addAttribute("pipe", pipe);
		model.addAttribute("message", "pipeline created and saved");

		this.edges=null;
		edges= new LinkedList<Edge>();
		this.nodes=null;
		nodes= new HashMap<String,Node>();
		this.addedInput=null;
		addedInput=new HashMap<Object,String>();

		try {

			ArrayList<Pipeline> pipeList = new ArrayList<>();
			pipeList = (ArrayList<Pipeline>) pipelineRepository.findByUser(user);
			model.addAttribute("pipeList", pipeList);	

		} catch (Exception e) {

			e.getMessage().toString();
		}

		return "kayakHome";
	}

	public String getUtenteConnesso() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name = auth.getName();
		return name;
	}

}
