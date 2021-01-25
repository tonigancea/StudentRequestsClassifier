package com.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.model.Student;
import com.repository.StudentRepository;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StudentRequestClassifierService {

	@Value("${spark.app.name}")
	private String appName;
	@Value("${spark.master}")
	private String masterUri;

	@Autowired
	private StudentRepository repository;

	JavaSparkContext sc = new JavaSparkContext();

//	public WordCountService() {
//		sc = new JavaSparkContext();

	private List<Student> students;

	public void init() {
		students = students = repository.findAll();
	}

	public long add(JSONObject request) throws JSONException {
		String surname = request.getString("surname");
		String name = request.getString("name");
		int choice = Integer.parseInt(request.getString("option"));
		String groups = "";
		String series = "";
		String colleagues = "";
		if(choice == 1) {
			groups = request.getString("group");
			series = request.getString("series");
		} else
			colleagues = request.getString("colleagues");

		for(Student student : students) {
			if(student.getSurname().equals(surname) && student.getName().equals(name)) {
				if(choice == 1) {
					student.setGroups(groups);
					student.setSeries(series);
				}
				else
					student.setColleagues(colleagues);

				if(student.getChoice() != choice && student.getChoice() != 3)
					student.setChoice(3);

				return student.getId();
			}
		}
		Student student = new Student(surname, name, choice, groups, series, colleagues);
		repository.save(student);
		return student.getId();
	}

	public Map<String, Long> getCount(List<String> wordList) {
		JavaRDD<String> words = sc.parallelize(wordList);
		Map<String, Long> wordCounts = words.countByValue();
		return wordCounts;
	}

}
