package com.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@GetMapping(path = "/cerere")
	public String send() {
		service.init();
		return "cerere";
	}

	@PostMapping(path = "/cerere")
	public void recieve(@RequestBody String body) throws JSONException {
		System.out.println(body);
		service.add(new JSONObject(body));
	}

	@GetMapping(path = "/statistici")
	public String send_statistics() {
		service.init();
		return "statistici";
	}

	@PostMapping(path = "/statistici")
	public ResponseEntity<String> recieve_statistics() throws JSONException {
		JSONObject body = new JSONObject();
		body.put("groups", service.groupStatistics());
		body.put("series", service.seriesStatistics());
		body.put("students", service.colleaguesStatistics());
		return new ResponseEntity<>(body.toString(), HttpStatus.OK);
	}

}
