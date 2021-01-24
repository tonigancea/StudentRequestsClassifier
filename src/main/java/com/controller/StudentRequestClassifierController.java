package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import com.service.StudentRequestClassifierService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class StudentRequestClassifierController {

	@Autowired
	StudentRequestClassifierService service;

	//private final StudentRepository repository;

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
		return "cerere";
	}

	@PostMapping(path = "/cerere")
	public void recieve(@RequestBody String body) {
		System.out.println(body);
	}

}
