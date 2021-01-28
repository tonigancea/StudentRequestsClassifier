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

	private JavaSparkContext sc;

	private boolean init = false;

	public void init() {
		if(init)
				return;
		init = true;
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
				return student.getId();
			}
		}
		Student student = new Student(surname, name, choice, groups, series, colleagues);
		repository.save(student);
		return student.getId();
	}

	public String groupStatistics() {
		String print = spacing("Group", Student.lengths[0]) + ' ' + spacing("Apparitions", 11);
		String groups = "";
		List<Student> students = repository.findAll();
		Map<String, Long> statistics;

		for(Student student : students)
			groups += student.getGroups() + " ";

		statistics = getCount(Arrays.asList(groups.split(" ")));

		for(String group : statistics.keySet())
			print += '\n' + spacing(group, Student.lengths[0]) + ' ' + spacing(Long.toString(statistics.get(group)), 11);

		return print;
	}

	public String seriesStatistics() {
		String print = spacing("Series", Student.lengths[1]) + ' ' + spacing("Apparitions", 11);
		String series = "";
		List<Student> students = repository.findAll();
		Map<String, Long> statistics;

		for(Student student : students)
			series += student.getSeries() + " ";

		statistics = getCount(Arrays.asList(series.split(" ")));

		for(String serie : statistics.keySet())
			print += '\n' + spacing(serie, Student.lengths[1]) + ' ' + spacing(Long.toString(statistics.get(serie)), 11);

		return print;
	}

	public String colleaguesStatistics() {
		String print = spacing("Student", Student.lengths[2]) + ' ' + spacing("Apparitions", 11);
		String colleagues = "";
		List<Student> students = repository.findAll();
		Map<String, Long> statistics;

		for(Student student : students)
			colleagues += student.getColleagues();

		statistics = getCount(Arrays.asList(colleagues.split("\\n")));

		for(String colleague : statistics.keySet())
			print += '\n' + spacing(colleague, Student.lengths[2]) + ' ' + spacing(Long.toString(statistics.get(colleague)), 11);

		return print;
	}

	public Map<String, Long> getCount(List<String> wordList) {
		JavaRDD<String> words = sc.parallelize(wordList);
		return words.countByValue();
	}

	public String spacing(String string, int length) {
		String spaced;

		length -= string.length();

		if(length % 2 == 0)
			spaced = string;
		else
			spaced = string + ' ';

		for(length /= 2; length > 0; length--)
			spaced = ' ' + spaced + ' ';

		return spaced;
	}
}
