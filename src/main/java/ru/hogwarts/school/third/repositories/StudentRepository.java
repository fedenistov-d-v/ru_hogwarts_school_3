package ru.hogwarts.school.third.repositories;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.third.model.Student;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Collection<Student> findByAge(Integer age);

    List<Student> findByAgeBetween(int startAge, int endAge);

    @Query(value = "SELECT count(*) FROM student", nativeQuery = true)
    int getCountStudentAll();

    @Query(value = "SELECT avg(age) FROM student", nativeQuery = true)
    float getAvgAgeStudents();

    @Query(value = "SELECT * FROM student ORDER BY id OFFSET (SELECT count(*) - 5 FROM student)", nativeQuery = true)
    List<Student> getLastFiveStudent();
}

