package ru.hogwarts.school.third.repositories;
import ru.hogwarts.school.third.model.Student;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Collection<Student> findByAge(Integer age);

    List<Student> findByAgeBetween(int startAge, int endAge);
}
