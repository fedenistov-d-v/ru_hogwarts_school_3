package ru.hogwarts.school.third.controllers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ru.hogwarts.school.third.model.Faculty;
import ru.hogwarts.school.third.model.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

//    В папке test/resources создаётся файл application.properties с подключением к тестовой базе данных, чтобы
//не вносить случайные изменения в основную БД.
//    Тестовая БД:
//- spring.datasource.url=jdbc:postgresql://localhost:5432/hogwarts-test
//- spring.datasource.username=student-test
//- spring.datasource.password=test.
//    В тестовой БД прописываются 5 студентов с возрастом от 1 до 100 лет, 2 из которых имеют возраст 20 лет,
// студент с Id=1 учится на факультете 11, 3 студента учатся на 1 факультете.
//    И 3 факультета:
//  id  color   name
//-  1	Белый	Истории
//-  2	Чёрный	География
//- 11	Голубой	Чёрный

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTestRT {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate template;

    @Test
    public void contextLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void testPost_createStudent() throws Exception {
        final String name = "Тестер_09";
        final int age = 81;

        ResponseEntity<Student> entityPost = template.postForEntity(
                "/student",
                new Student(0L, age, name, new Faculty(2L, "string", "string")),
                Student.class);
        assertEquals(HttpStatus.CREATED, entityPost.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, entityPost.getHeaders().getContentType());

        Student result = entityPost.getBody();
        assert result != null;
        assertEquals(name, result.getName());
        assertEquals(age, result.getAge());

        deleteStudentTest(result.getId());
    }

    @Test
    public void testGet_findByAge() throws Exception {
        int age = 20;
        ResponseEntity<Student[]> entity = template.getForEntity("/student?age=" + age, Student[].class);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, entity.getHeaders().getContentType());

        Student[] students = entity.getBody();
        assert students != null;
        assertEquals(2, students.length);
        assertEquals(age, students[0].getAge());
    }

    @Test
    public void testGet_getStudentById() throws Exception {
        int id = 1;
        ResponseEntity<Student> entity = template.getForEntity("/student/" + id, Student.class);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, entity.getHeaders().getContentType());

        Student student = entity.getBody();
        assert student != null;
        assertEquals(id, student.getId());
    }

    @Test
    public void testGet_findByAgeBetween() throws Exception {
        ResponseEntity<Student[]> entity = template.getForEntity("/student/between", Student[].class);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, entity.getHeaders().getContentType());

        Student[] students = entity.getBody();
        assert students != null;
        assertEquals(5, students.length);
    }

    @Test
    public void testGet_getFacultyByStudentId() throws Exception {
        int id = 1;
        int facultyId = 11;
        ResponseEntity<Faculty> entity = template.getForEntity("/student/" + id + "/faculty", Faculty.class);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, entity.getHeaders().getContentType());

        Faculty faculty = entity.getBody();
        assert faculty != null;
        assertEquals(facultyId, faculty.getId());
    }

    @Test
    public void testPut_editStudent() throws Exception {
        ResponseEntity<Student> entityPost = template.postForEntity(
                "/student",
                new Student(
                        0L,
                        82,
                        "Тестер_редактор",
                        new Faculty(2L, "string", "string")),
                Student.class);
        assertEquals(HttpStatus.CREATED, entityPost.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, entityPost.getHeaders().getContentType());
        assert entityPost.getBody() != null;

        final int age = 5;
        final String name = String.format("Апдатер_%2d", age);

        ResponseEntity<Student> entityPut = template.exchange(
                "/student",
                HttpMethod.PUT,
                new HttpEntity<Student>(new Student(
                        entityPost.getBody().getId(),
                        age,
                        name,
                        new Faculty(1L, "string", "string"))),
                Student.class);
        assertEquals(HttpStatus.OK, entityPut.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, entityPut.getHeaders().getContentType());

        Student putResult = entityPut.getBody();
        assert putResult != null;
        assertEquals(name, putResult.getName());
        assertEquals(age, putResult.getAge());

        deleteStudentTest(putResult.getId());
    }

    @Test
    public void testDelete_deleteStudent() throws Exception {
        final Student newStudent = new Student(
                0L,
                83,
                "Тестер_на_удаление",
                new Faculty(2L, "string", "string"));
        ResponseEntity<Student> entityPost = template.postForEntity("/student", newStudent, Student.class);
        Student result = entityPost.getBody();
        assert result != null;

        HttpEntity<Student> entity = new HttpEntity<Student>(new Student());

        ResponseEntity<Student> entityDelete = template.exchange("/student/" + result.getId(), HttpMethod.DELETE,
                entity, new ParameterizedTypeReference<Student>() {
                });
        assertEquals(HttpStatus.OK, entityDelete.getStatusCode());
        assertNull(entityDelete.getHeaders().getContentType());
    }

    private void deleteStudentTest(long id) throws Exception {
        HttpEntity<Student> entity = new HttpEntity<Student>(new Student());

        ResponseEntity<Student> entityPut = template.exchange("/student/" + id, HttpMethod.DELETE,
                entity, new ParameterizedTypeReference<Student>() {
                });
        assertEquals(HttpStatus.OK, entityPut.getStatusCode());
    }
}
