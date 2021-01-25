package com.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String surname;

    private String name;

    private int choice;

    private String groups;

    private String series;

    private String colleagues;

    public Student(String surname, String name, int choice, String groups, String series, String colleagues) {
        this.surname = surname;
        this.name = name;
        this.choice = choice;
        this.groups = groups;
        this.series = series;
        this.colleagues = colleagues;
    }

    public Student() {

    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getSurname() { return surname;  }

    public void setSurname(String surname) { this.surname = surname; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getChoice() { return choice; }

    public void setChoice(int choice)  { this.choice = choice; }

    public String getGroups() { return groups; }

    public void setGroups(String groups) { this.groups = groups; }

    public String getSeries() { return series; }

    public void setSeries(String series) { this.series = series; }

    public String getColleagues() { return colleagues; }

    public void setColleagues(String series) { this.colleagues = colleagues; }

}
