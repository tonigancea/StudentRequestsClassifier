package com.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import com.service.StudentRequestClassifierService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class StudentRequestClassifierController {

	@Autowired
	private StudentRequestClassifierService service;





	//public WordCountController(StudentRepository repository) {
	//	this.repository = repository;
	//}
	@RequestMapping("/wordcount")
	public Map<String, Long> count(@RequestParam(required = false) String words) {
		List<String> wordList = Arrays.asList(words.split("\\|"));
		return service.getCount(wordList);
	}

	@GetMapping(path = "/cerere")
	public String send() {
		service.init();
		return "cerere";
	}

	@PostMapping(path = "/cerere")
	public void recieve(@RequestBody String body) throws JSONException {
		service.add(new JSONObject(body));
		System.out.println(body);
	}

}
