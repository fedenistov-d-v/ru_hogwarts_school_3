package ru.hogwarts.school.third.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity(name = "faculty")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Faculty {

    @Id
    @GeneratedValue
    private long id;

    private String name;
    private String color;

    @OneToMany(mappedBy = "oneFaculty", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<Student> students;

    @JsonIgnore
    public Collection<Student> getStudents() {
        return students;
    }

    public Faculty() {
    }

    public Faculty(long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public void setStudents(Collection<Student> students) {
        this.students = students;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Faculty faculty = (Faculty) o;
        return id == faculty.id && Objects.equals(name, faculty.name) && Objects.equals(color, faculty.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
