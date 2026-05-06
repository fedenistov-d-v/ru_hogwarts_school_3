package ru.hogwarts.school.third.repositories;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.third.model.Faculty;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Collection<Faculty> findByColor(String color);

    Collection<Faculty> findByNameIgnoreCaseOrColorIgnoreCase(String name, String color);

    Collection<Faculty> findByNameContainsIgnoreCase(String name);

    @Query(value = "SELECT name FROM faculty", nativeQuery = true)
    List<String> findAllNameFaculty();
}
