package ru.hogwarts.school.third.controllers;
import ru.hogwarts.school.third.model.Faculty;
import ru.hogwarts.school.third.model.Student;
import ru.hogwarts.school.third.services.StudentService;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTestWebMvc {

    final long ID = 1L;
    final int AGE = 25;
    final String NAME = "Даша";
    final long FACULTY_ID = 2L;
    final String FACULTY_NAME = "Java";
    final String FACULTY_COLOR = "white";

    Student student = new Student();
    Faculty faculty = new Faculty();
    JSONObject studentObject = new JSONObject();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @BeforeEach
    void storageServiceMock() throws JSONException {
        faculty.setId(FACULTY_ID);
        faculty.setName(FACULTY_NAME);
        faculty.setColor(FACULTY_COLOR);

        student.setId(ID);
        student.setAge(AGE);
        student.setName(NAME);
        student.setOneFaculty(faculty);

        studentObject.put("id", ID);
        studentObject.put("age", AGE);
        studentObject.put("name", NAME);
    }

    @Test
    public void testPost_createStudent() throws Exception {
        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.age").value(AGE))
                .andExpect(jsonPath("$.name").value(NAME));
    }

    @Test
    public void testGet_findByAge() throws Exception {
        when(studentService.findByAge(AGE)).thenReturn(List.of(student));

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/student?age=" + AGE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].age").value(AGE))
                .andExpect(jsonPath("$[0].name").value(NAME));
    }

    @Test
    public void testGet_getStudentById() throws Exception {
        when(studentService.findById(ID)).thenReturn(student);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/student/" + ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.age").value(AGE))
                .andExpect(jsonPath("$.name").value(NAME));
    }

    @Test
    public void testGet_findByAgeBetween() throws Exception {
        when(studentService.findByAgeBetween(1, 100)).thenReturn(List.of(student));

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/student/between")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].age").value(AGE))
                .andExpect(jsonPath("$[0].name").value(NAME));
    }

    @Test
    public void testGet_getFacultyByStudentId() throws Exception {
        when(studentService.findById(ID)).thenReturn(student);
        when(studentService.getFacultyByStudentId(ID)).thenReturn(student.getOneFaculty());

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/student/" + ID + "/faculty")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(FACULTY_ID))
                .andExpect(jsonPath("$.color").value(FACULTY_COLOR))
                .andExpect(jsonPath("$.name").value(FACULTY_NAME));
    }

    @Test
    public void testPut_editStudent() throws Exception {
        when(studentService.updateStudent(student)).thenReturn(student);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.age").value(AGE))
                .andExpect(jsonPath("$.name").value(NAME));
    }

    @Test
    public void testDelete_deleteStudent() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/student/" + ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
