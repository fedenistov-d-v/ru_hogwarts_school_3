package ru.hogwarts.school.third.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "student") //Когда name совпадает с именем класса этот парамерт не обязателен
public class Student {

    @Id
    @GeneratedValue
    private long id;

    private int age;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id")
    private Faculty oneFaculty;

    public Student() {}

    public Student(long id, int age, String name, Faculty oneFaculty) {
        this.id = id;
        this.age = age;
        this.name = name;
        this.oneFaculty = oneFaculty;
    }

    public Faculty getOneFaculty() {
        return oneFaculty;
    }

    public void setOneFaculty(Faculty oneFaculty) {
        this.oneFaculty = oneFaculty;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", age=" + age +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id && age == student.age && Objects.equals(name, student.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, age, name);
    }
}
