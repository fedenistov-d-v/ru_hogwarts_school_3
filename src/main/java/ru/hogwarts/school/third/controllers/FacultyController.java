package ru.hogwarts.school.third.controllers;
import org.springframework.http.HttpStatus;
import ru.hogwarts.school.third.model.Faculty;
import ru.hogwarts.school.third.model.Student;
import ru.hogwarts.school.third.services.FacultyService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        return ResponseEntity.status(HttpStatus.CREATED).body(facultyService.createFaculty(faculty));
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFacultyById(@PathVariable long id) {
        Faculty faculty = facultyService.findById(id);
        if (faculty == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(faculty);
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> findByColor(@RequestParam(required = false) String color) {
        if (color != null && !color.isBlank()) return ResponseEntity.ok(facultyService.findByColor(color));
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/find_name_or_color")
    public ResponseEntity<Collection<Faculty>> findString(@RequestParam(required = false) String string) {
        if (string != null && !string.isBlank()) return ResponseEntity.ok(facultyService.findByNameOrColor(string));
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<Collection<Student>> getStudentsByFacultyId(@PathVariable(required = false) long id) {
        Faculty foundFaculty = facultyService.findById(id);
        if (foundFaculty == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(facultyService.getStudentsByFacultyId(id));
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty foundFaculty = facultyService.updateFaculty(faculty);
        if (foundFaculty == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable long id) {
        facultyService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
