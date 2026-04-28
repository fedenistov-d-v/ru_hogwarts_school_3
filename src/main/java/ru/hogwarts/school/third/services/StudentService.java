package ru.hogwarts.school.third.services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hogwarts.school.third.model.Faculty;
import ru.hogwarts.school.third.model.Student;
import ru.hogwarts.school.third.repositories.StudentRepository;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Был вызван метод для создания записи студента с параметром ({})", student.toString());
        return  studentRepository.save(student);
    }

    public Student findById(long id) {
        logger.info("Был вызван метод для поиска записи студента с id ({})", id);
        return studentRepository.findById(id).get();
    }

    public Student updateStudent(Student student) {
        logger.info("Был вызван метод для изменения данных студента с параметром ({})", student.toString());
        if (studentRepository.findById(student.getId()).isEmpty()) {
            logger.error("Нет записи о студенте в базе данных с id={}", student.getId());
            return null;
        }
        return studentRepository.save(student);
    }

    public void deleteById(long id) {
        logger.info("Был вызван метод для удаления записи студента с id ({})", id);
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAllStudent() {
        logger.info("Был вызван метод - показать всех студентов.");
        return studentRepository.findAll();
    }

    public Collection<Student> findByAge(Integer age) {
        logger.info("Был вызван метод для поиска студентов с возрастом = {} ", age);
        return studentRepository.findByAge(age);
    }

    public List<Student> findByAgeBetween(int minAge, int maxAge) {
        logger.info("Был вызван метод для поиска студентов по возрасту в интервале от {} до {}.", minAge, maxAge);
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Faculty getFacultyByStudentId(long id) {
        logger.info("Был вызван метод для просмотра факультета студента с id={}.", id);
        return studentRepository.getReferenceById(id).getOneFaculty();
    }

    public int getCountAllStudent() {
        logger.info("Был вызван метод для просмотра количества записей студентов в базе данных.");
        return studentRepository.getCountStudentAll();
    }

    public float getAvgAgeStudents() {
        logger.info("Был вызван метод подсчёта среднего возраста студентов в базе данных.");
        if (studentRepository.findAll().isEmpty()) {
            logger.error("Нет ни одной записи о студенте в базе данных.");
            return 0.0F;
        }
        return studentRepository.getAvgAgeStudents();
    }

    public List<Student> getLastFiveStudents() {
        logger.info("Был вызван метод - последние 5 записей в базе данных о студентах.");
        return studentRepository.getLastFiveStudent();
    }
}
