package ru.hogwarts.school.third.services;
import ru.hogwarts.school.third.model.Faculty;
import ru.hogwarts.school.third.model.Student;
import ru.hogwarts.school.third.repositories.FacultyRepository;

import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findById(long id) {
        return facultyRepository.findById(id).get();
    }

    public Faculty updateFaculty(Faculty faculty) {
        if (facultyRepository.findById(faculty.getId()).isEmpty()) return null;
        return facultyRepository.save(faculty);
    }

    public void deleteById(long id) {
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }

    public Collection<Faculty> findByColor(String color) {
        return facultyRepository.findByColor(color);
    }

    public Collection<Faculty> findByNameOrColor(String string) {
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(string, string);
    }

    public Collection<Student> getStudentsByFacultyId(long id) {
        return facultyRepository.getReferenceById(id).getStudents();
    }
}
