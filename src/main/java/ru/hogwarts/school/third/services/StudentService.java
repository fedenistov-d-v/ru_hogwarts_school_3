package ru.hogwarts.school.third.services;
import ru.hogwarts.school.third.model.Faculty;
import ru.hogwarts.school.third.model.Student;
import ru.hogwarts.school.third.repositories.StudentRepository;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return  studentRepository.save(student);
    }

    public Student findById(long id) {
        return studentRepository.findById(id).get();
    }

    public Student updateStudent(Student student) {
        if (studentRepository.findById(student.getId()).isEmpty()) return null;
        return studentRepository.save(student);
    }

    public void deleteById(long id) {
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAllStudent() {
        return studentRepository.findAll();
    }

    public Collection<Student> findByAge(Integer age) {
        return studentRepository.findByAge(age);
    }

    public List<Student> findByAgeBetween(int minAge, int maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Faculty getFacultyByStudentId(long id) {
        return studentRepository.getReferenceById(id).getOneFaculty();
    }

    public int getCountAllStudent() {
        return studentRepository.getCountStudentAll();
    }

    public float getAvgAgeStudents() {
        if (studentRepository.findAll().isEmpty()) return 0.0F;
        return studentRepository.getAvgAgeStudents();
    }

    public List<Student> getLastFiveStudents() {
        return studentRepository.getLastFiveStudent();
    }
}
