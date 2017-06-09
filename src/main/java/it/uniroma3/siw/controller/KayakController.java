package it.uniroma3.siw.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class KayakController {
	
	private String propertiesConfigFilePath = "/application.properties";
	
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

}
