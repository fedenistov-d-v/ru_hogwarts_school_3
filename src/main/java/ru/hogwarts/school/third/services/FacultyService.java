package ru.hogwarts.school.third.services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hogwarts.school.third.model.Faculty;
import ru.hogwarts.school.third.model.Student;
import ru.hogwarts.school.third.repositories.FacultyRepository;

import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Был вызван метод для создания записи факультета с параметром ({})", faculty.toString());
        return facultyRepository.save(faculty);
    }

    public Faculty findById(long id) {
        logger.info("Был вызван метод для поиска записи факультета с id ({})", id);
        return facultyRepository.findById(id).get();
    }

    public Faculty updateFaculty(Faculty faculty) {
        logger.info("Был вызван метод для изменения данных факультета с параметром ({})", faculty.toString());
        if (facultyRepository.findById(faculty.getId()).isEmpty()) {
            logger.error("Нет записи о факультете в базе данных с id={}", faculty.getId());
            return null;
        }
        return facultyRepository.save(faculty);
    }

    public void deleteById(long id) {
        logger.info("Был вызван метод для удаления записи факультета с id ({})", id);
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getAllFaculty() {
        logger.info("Был вызван метод - показать все факультеты.");
        return facultyRepository.findAll();
    }

    public Collection<Faculty> findByColor(String color) {
        logger.info("Был вызван метод для поиска факультетов с color={} ", color);
        return facultyRepository.findByColor(color);
    }

    public Collection<Faculty> findByNameOrColor(String string) {
        logger.info("Был вызван метод для поиска факультетов по имени или цвету с параметром = {}", string);
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(string, string);
    }

    public Collection<Student> getStudentsByFacultyId(long id) {
        logger.info("Был вызван метод показать всех студентов факультета с id={}.", id);
        return facultyRepository.getReferenceById(id).getStudents();
    }
}
