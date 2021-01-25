package com.service;

import java.util.ArrayList;
import java.util.Arrays;
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

	JavaSparkContext sc;

	public void init() {
		System.out.println("lol");
		sc = new JavaSparkContext(new SparkConf().setAppName("Student Request Classifier").setMaster("local"));
	}

	public long add(JSONObject request) throws JSONException {
		List<Student> students = repository.findAll();
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
				else {
					System.out.println(colleagues);
					student.setColleagues(colleagues);
				}
				if(student.getChoice() != choice && student.getChoice() != 3)
					student.setChoice(3);
				repository.save(student);
				groupStatistics();
				seriesStatistics();
				colleaguesStatistics();
				return student.getId();
			}
		}
		Student student = new Student(surname, name, choice, groups, series, colleagues);
		repository.save(student);
		groupStatistics();
		seriesStatistics();
		colleaguesStatistics();
		return student.getId();
	}

	public void groupStatistics() {
		List<Student> students = repository.findAll();
		String groups = "";
		for(Student student : students)
			groups += student.getGroups() + " ";
		Map<String, Long> statistics = getCount(Arrays.asList(groups.split(" ")));
		System.out.println("Group Number of apparitions");
		for(String group : statistics.keySet())
			System.out.println(" " + group + "  " + statistics.get(group));
	}

	public void seriesStatistics() {
		List<Student> students = repository.findAll();
		String series = "";
		for(Student student : students)
			series += student.getSeries() + " ";
		Map<String, Long> statistics = getCount(Arrays.asList(series.split(" ")));
		System.out.println("Series Number of apparitions");
		for(String serie : statistics.keySet())
			System.out.println("  " + serie + "   " + statistics.get(serie));
	}

	public void colleaguesStatistics() {
		List<Student> students = repository.findAll();
		String colleagues = "";
		for(Student student : students)
			colleagues += student.getColleagues();
		Map<String, Long> statistics = getCount(Arrays.asList(colleagues.split("\\n")));
		System.out.println("Colleague Number of apparitions");
		for(String colleague : statistics.keySet())
			System.out.println(" " + colleague + "  " + statistics.get(colleague));
	}

	public Map<String, Long> getCount(List<String> wordList) {
		JavaRDD<String> words = sc.parallelize(wordList);
		return words.countByValue();
	}

}
