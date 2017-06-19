package it.uniroma3.siw.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import it.uniroma3.siw.model.Edge;
import it.uniroma3.siw.model.InputNode;
import it.uniroma3.siw.model.Node;
import it.uniroma3.siw.model.Pipeline;
import it.uniroma3.siw.model.StageNode;
import it.uniroma3.siw.repository.EdgeRepository;
import it.uniroma3.siw.repository.PipelineRepository;
import it.uniroma3.siw.repository.UtenteRepository;
import it.uniroma3.siw.service.PipelineService;
import it.uniroma3.siw.service.UtenteService;

@Controller
@SuppressWarnings("unused")
public class ModifyPipeController {

	@Autowired
	private PipelineRepository pipelineRepository;
	@Autowired
	private PipelineService pipelineService;
	@Autowired
	private UtenteRepository utenteRepository;
	@Autowired
	private UtenteService utenteService;
	@Autowired
	private EdgeRepository edgeRepository;

	private RestTemplate restTemplate = new RestTemplate();
	@SuppressWarnings("unchecked")
	private LinkedList<String> prList	 = (LinkedList<String>) restTemplate.getForObject("http://kayakmockbackend.eu-west-2.elasticbeanstalk.com/rest/primitiveList", LinkedList.class);

	private HashMap<Object,String> addedInput = new HashMap<Object,String>();
	private HashMap<String,Node> nodes = new HashMap<String,Node>();


	@PostMapping("/removeEdge")
	public String removeEdge(@RequestParam String pipeId ,@RequestParam String id,Model model) {


		ArrayList<Pipeline> pipeL = (ArrayList<Pipeline>) pipelineRepository.findAllById(Long.valueOf(pipeId));
		Pipeline pipe = pipeL.get(0);

		ArrayList<Edge> PipeEdges = (ArrayList<Edge>) edgeRepository.findAllById(Long.valueOf(id));
		Edge edge = PipeEdges.get(0);

		List<Edge> edges = pipe.getEdges();
		edges.remove(edge);

		pipe.setEdges(edges);

		pipelineService.add(pipe);
		edgeRepository.deleteById(Long.valueOf(id));

		model.addAttribute("pipe",pipe);

		ArrayList <Edge> linkList =(ArrayList<Edge>) edgeRepository.findByPipeline(pipe);

		ArrayList <String> linkListStamp = new ArrayList<String>();
		for (Edge e : linkList) linkListStamp.add("id : " + e.getId() + " items : " +  e.getSxItem().toString() + " ----> " + e.getDxItem().toString());

		model.addAttribute("prList", prList);
		
		model.addAttribute("linkList", linkList);
		model.addAttribute("linkListStamp", linkListStamp);

		return "modifyPipe";

	}

	@PostMapping("/addInputPipe")
	public String addInput(ModelMap model, @RequestParam String filename, @RequestParam String pipeId) {

		InputNode node = new InputNode();
		node.setFileName(filename);

		nodes.put(filename,node);

		ArrayList<Pipeline> pipeL = (ArrayList<Pipeline>) pipelineRepository.findAllById(Long.valueOf(pipeId));
		Pipeline pipe = pipeL.get(0);

		model.addAttribute("pipe",pipe);

		ArrayList <Edge> linkList =(ArrayList<Edge>) edgeRepository.findByPipeline(pipe);

		ArrayList <String> linkListStamp = new ArrayList<String>();
		for (Edge e : linkList) linkListStamp.add("id : " + e.getId() + " items : " +  e.getSxItem().toString() + " ----> " + e.getDxItem().toString());

		model.addAttribute("linkList", linkList);
		model.addAttribute("linkListStamp", linkListStamp);


		addedInput.put(filename,"input");
		model.addAttribute("addedInput", addedInput);			
		model.addAttribute("prList", prList);

		return "modifyPipe";
	}

	@PostMapping("/addStagePipe")
	public String addStage(ModelMap model, @RequestParam String stage, @RequestParam String pipeId) {

		StageNode node = new StageNode();
		node.setStageType(stage);

		nodes.put(stage,node);

		ArrayList<Pipeline> pipeL = (ArrayList<Pipeline>) pipelineRepository.findAllById(Long.valueOf(pipeId));
		Pipeline pipe = pipeL.get(0);

		model.addAttribute("pipe",pipe);

		ArrayList <Edge> linkList =(ArrayList<Edge>) edgeRepository.findByPipeline(pipe);

		ArrayList <String> linkListStamp = new ArrayList<String>();
		for (Edge e : linkList) linkListStamp.add("id : " + e.getId() + " items : " +  e.getSxItem().toString() + " ----> " + e.getDxItem().toString());

		model.addAttribute("linkList", linkList);
		model.addAttribute("linkListStamp", linkListStamp);

		addedInput.put(stage,"stage");
		model.addAttribute("addedInput", addedInput);				
		model.addAttribute("prList", prList);

		return "modifyPipe";
	}

	@PostMapping("/createLinkPipe")
	public String createLink(ModelMap model, @RequestParam String sxItem, @RequestParam String dxItem, @RequestParam String pipeId) {

		model.addAttribute("prList", prList);
		model.addAttribute("addedInput", addedInput);

		Edge edge = new Edge();

		edge.setDxItem(nodes.get(dxItem));
		edge.setSxItem(nodes.get(sxItem));	

		ArrayList<Pipeline> pipeL = (ArrayList<Pipeline>) pipelineRepository.findAllById(Long.valueOf(pipeId));
		Pipeline pipe = pipeL.get(0);

		List<Edge> pipeEdges = pipe.getEdges();
		pipeEdges.add(edge);

		pipe.setEdges(pipeEdges);

		pipelineService.add(pipe);

		ArrayList <Edge> linkList =(ArrayList<Edge>) edgeRepository.findByPipeline(pipe);

		ArrayList <String> linkListStamp = new ArrayList<String>();
		for (Edge e : linkList) linkListStamp.add("id : " + e.getId() + " items : " +  e.getSxItem().toString() + " ----> " + e.getDxItem().toString());

		model.addAttribute("linkList", linkList);
		model.addAttribute("linkListStamp", linkListStamp);
		model.addAttribute("pipe",pipe);
		model.addAttribute("addedInput", addedInput);			
		model.addAttribute("prList", prList);

		return "modifyPipe";
	}

}

