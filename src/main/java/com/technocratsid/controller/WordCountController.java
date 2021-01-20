package com.technocratsid.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;

import com.technocratsid.service.WordCountService;

@Controller
public class WordCountController {

	@Autowired
	WordCountService service;

	@GetMapping(path = "/wordcount")
	public Map<String, Long> count(@RequestParam(required = false) String words) {
		List<String> wordList = Arrays.asList(words.split("\\|"));
		return service.getCount(wordList);
	}

	@GetMapping(path = "/cerere")
	public String index() {
		return "cerere";
	}

}
