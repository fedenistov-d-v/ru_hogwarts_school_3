package ru.hogwarts.school.third.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hogwarts.school.third.model.Faculty;
import ru.hogwarts.school.third.model.Student;
import ru.hogwarts.school.third.repositories.StudentRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        return studentRepository.save(student);
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

    public List<Student> getAllStudent() {
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

    public double getAvgAgeStudents() {
        logger.info("Был вызван метод подсчёта среднего возраста студентов в базе данных.");
        if (studentRepository.findAll().isEmpty()) {
            logger.error("Нет ни одной записи о студенте в базе данных.");
            return 0.0;
        }
        return studentRepository.getAvgAgeStudents();
    }

    public double getAvgAgeStudentsStream() {
        logger.info("Был вызван метод подсчёта среднего возраста студентов с использованием Stream.");
        return studentRepository.findAll().stream()
                .parallel()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);
    }

    public List<Student> getLastFiveStudents() {
        logger.info("Был вызван метод - последние 5 записей в базе данных о студентах.");
        return studentRepository.getLastFiveStudent();
    }

    public List<String> getNamesBeginningA() {
        logger.info("Был вызван метод - показать имена всех студентов на 'А'.");
        return studentRepository.findAll().stream()
                .parallel()
                .map(s -> s.getName().toUpperCase())
                .filter(str -> str.charAt(0) == 'А' || str.charAt(0) == 'A')
                .sorted()
                .toList();
    }

    public void getSixStudentStreamParallel() {
        List<Student> students = studentRepository.findAll();
        List<Thread> threads = new ArrayList<>();

        threads.add(getThread(students, 2,4));
        threads.add(getThread(students, 4,6));

        System.out.println(students.get(0));
        System.out.println(students.get(1));
        for (Thread thread : threads) {
            thread.start();
        }
    }

    public void getSixStudentSynchronized() {
        List<Student> students = studentRepository.findAll();
        List<Thread> threads = new ArrayList<>();

        threads.add(getThreadSynchronized(students, 2,4));
        threads.add(getThreadSynchronized(students, 4,6));

        printName(students.get(0).toString());
        printName(students.get(1).toString());
        for (Thread thread : threads) {
            thread.start();
        }
    }

    public void getAllStudentSynchronized() {
        final int NUMBER_THREAD = 3;
        int start = 2;
        List<Student> students = studentRepository.findAll();
        List<Thread> threads = new ArrayList<>();
        int numberStudentInThread = (students.size() - start) / NUMBER_THREAD;
        if ((students.size() - start) % NUMBER_THREAD != 0) numberStudentInThread++;
        int end = start + numberStudentInThread;

        for (int i = 0; i < NUMBER_THREAD; i++) {
            if (i == 2) end = students.size();
            threads.add(getThreadSynchronized(students, start, end));
            start = end;
            end += numberStudentInThread;
        }

        printName(students.get(0).toString());
        printName(students.get(1).toString());

        for (Thread thread : threads) {
            thread.start();
        }
    }

    private Thread getThread(List<Student> students, int start, int end) {
        return new Thread(() -> {
            for (int i = start; i < end; i++) {
                System.out.println(students.get(i));
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private Thread getThreadSynchronized(List<Student> students, int start, int end) {
        return new Thread(() -> {
            for (int i = start; i < end; i++) {
                printName(students.get(i).toString());
            }
        });
    }

    private synchronized void printName(String name) {
        System.out.println(name);
    }
}
