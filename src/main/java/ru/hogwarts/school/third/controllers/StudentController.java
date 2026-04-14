package ru.hogwarts.school.third.controllers;
import org.springframework.http.HttpStatus;
import ru.hogwarts.school.third.model.Faculty;
import ru.hogwarts.school.third.model.Student;
import ru.hogwarts.school.third.services.StudentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.createStudent(student));
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable long id) {
        Student student = studentService.findById(id);
        if (student == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(student);
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> findByAge(@RequestParam(required = false) Integer age) {
        if (age != null && age >0) return ResponseEntity.ok(studentService.findByAge(age));
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/between")
    public ResponseEntity<Collection<Student>> findByAgeBetween(@RequestParam(defaultValue = "1") Integer minAge,
                                                                @RequestParam(defaultValue = "100") Integer maxAge) {
        if (minAge > 0 && maxAge > minAge) {
            return ResponseEntity.ok(studentService.findByAgeBetween(minAge, maxAge));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<Faculty> getFacultyByStudentId(@PathVariable long id) {
        Student foundStudent = studentService.findById(id);
        if (foundStudent == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(studentService.getFacultyByStudentId(id));
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.updateStudent(student);
        if (foundStudent == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable long id) {
        studentService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
