package ru.hogwarts.school.third.controllers;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ru.hogwarts.school.third.model.Faculty;
import ru.hogwarts.school.third.model.Student;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

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
public class FacultyControllerTestRT {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate template;

    @Test
    public void contextLoads() throws Exception {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    public void testPost_createFaculty() throws Exception {
        final String NAME = "Тестов_01";
        final String COLOR = "тестовый";

        ResponseEntity<Faculty> entityPost = template.postForEntity(
                "/faculty",
                new Faculty(0L, NAME, COLOR),
                Faculty.class);

        assertEquals(HttpStatus.CREATED, entityPost.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, entityPost.getHeaders().getContentType());

        Faculty createdFaculty = entityPost.getBody();
        assert createdFaculty != null;
        assertEquals(NAME, createdFaculty.getName());
        assertEquals(COLOR, createdFaculty.getColor());

        deleteResultTestes(createdFaculty.getId());
    }

    @Test
    public void testGet_getFacultyById() throws Exception {
        int id = 1;

        ResponseEntity<Faculty> result = template.getForEntity("/faculty/" + id, Faculty.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, result.getHeaders().getContentType());

        Faculty faculty = result.getBody();
        assert faculty != null;
        assertEquals(id, faculty.getId());
    }

    @Test
    public void testGet_findByColor() throws Exception {
        String color = "Чёрный";

        ResponseEntity<Faculty[]> result = template.getForEntity("/faculty?color=" + color, Faculty[].class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, result.getHeaders().getContentType());

        Faculty[] faculties = result.getBody();
        assert faculties != null;
        assertEquals(1, faculties.length);
        assertEquals(color, faculties[0].getColor());
    }

    @Test
    public void testGet_findString() throws Exception {
        String string = "чёРный";

        ResponseEntity<Faculty[]> result = template
                .getForEntity("/faculty/find_name_or_color?string=" + string, Faculty[].class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, result.getHeaders().getContentType());

        Faculty[] faculties = result.getBody();
        assert faculties != null;
        assertEquals(2, faculties.length);
        assert string.equalsIgnoreCase(faculties[0].getName()) ||
                string.equalsIgnoreCase(faculties[0].getColor());
    }

    @Test
    public void testGet_getStudentsByFacultyId() throws Exception {
        int id = 1;

        ResponseEntity<Student[]> result = template.getForEntity("/faculty/" + id + "/students", Student[].class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, result.getHeaders().getContentType());

        Student[] students = result.getBody();
        assert students != null;
        assertEquals(3, students.length);
        assertEquals(id, students[0].getOneFaculty().getId());
    }

    @Test
    public void testPut_editFaculty() throws Exception {
        ResponseEntity<Faculty> entityPost = template.postForEntity(
                "/faculty",
                new Faculty(0L, "Тест на редактирование", "тестовый"),
                Faculty.class);
        assertEquals(HttpStatus.CREATED, entityPost.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, entityPost.getHeaders().getContentType());
        assert entityPost.getBody() != null;

        final String COLOR = "лучший";
        final String NAME = String.format("Факультет %s", COLOR);

        ResponseEntity<Faculty> entityPut = template.exchange(
                "/faculty",
                HttpMethod.PUT,
                new HttpEntity<Faculty>(new Faculty(entityPost.getBody().getId(), NAME, COLOR)),
                Faculty.class);

        assertEquals(HttpStatus.OK, entityPut.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, entityPut.getHeaders().getContentType());

        Faculty createdFaculty = entityPut.getBody();
        assert createdFaculty != null;
        assertEquals(NAME, createdFaculty.getName());
        assertEquals(COLOR, createdFaculty.getColor());

        deleteResultTestes(createdFaculty.getId());
    }

    @Test
    public void testDelete_deleteFaculty() throws Exception {
        ResponseEntity<Faculty> entityPost = template.postForEntity(
                "/faculty",
                new Faculty(0L, "Тест на удаление", "тестовый"),
                Faculty.class);
        assertEquals(HttpStatus.CREATED, entityPost.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, entityPost.getHeaders().getContentType());
        assert entityPost.getBody() != null;

        ResponseEntity<Faculty> entityDelete = template.exchange(
                "/faculty/" + entityPost.getBody().getId(),
                HttpMethod.DELETE,
                new HttpEntity<Faculty>(new Faculty()),
                Faculty.class);

        assertEquals(HttpStatus.OK, entityDelete.getStatusCode());
        assertNull(entityDelete.getHeaders().getContentType());
    }

    private void deleteResultTestes(long id) {
        HttpEntity<Faculty> entity = new HttpEntity<Faculty>(new Faculty());

        ResponseEntity<Faculty> entityPut = template.exchange("/faculty/" + id, HttpMethod.DELETE,
                entity, new ParameterizedTypeReference<Faculty>() {
                });
        assertEquals(HttpStatus.OK, entityPut.getStatusCode());
    }
}
