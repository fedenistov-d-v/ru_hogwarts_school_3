package ru.hogwarts.school.third.repositories;
import ru.hogwarts.school.third.model.Faculty;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Collection<Faculty> findByColor(String color);

    Collection<Faculty> findByNameIgnoreCaseOrColorIgnoreCase(String name, String color);

    Collection<Faculty> findByNameContainsIgnoreCase(String name);
}
