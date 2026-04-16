package ru.hogwarts.school.third.controllers;
import ru.hogwarts.school.third.model.Faculty;
import ru.hogwarts.school.third.model.Student;
import ru.hogwarts.school.third.services.FacultyService;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(FacultyController.class)
public class FacultyControllerTestWebMvc {

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
    private FacultyService facultyService;

    @BeforeEach
    void storageServiceMock() throws JSONException {
        faculty.setId(FACULTY_ID);
        faculty.setName(FACULTY_NAME);
        faculty.setColor(FACULTY_COLOR);
        faculty.setStudents(List.of(student));

        student.setId(ID);
        student.setAge(AGE);
        student.setName(NAME);
        student.setOneFaculty(faculty);

        studentObject.put("id", FACULTY_ID);
        studentObject.put("name", FACULTY_NAME);
        studentObject.put("color", FACULTY_COLOR);
    }

    @Test
    public void testPost_createFaculty() throws Exception {
        when(facultyService.createFaculty(faculty)).thenReturn(faculty);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(FACULTY_ID))
                .andExpect(jsonPath("$.name").value(FACULTY_NAME))
                .andExpect(jsonPath("$.color").value(FACULTY_COLOR));
    }

    @Test
    public void testGet_getFacultyById() throws Exception {
        when(facultyService.findById(FACULTY_ID)).thenReturn(faculty);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/faculty/" + FACULTY_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(FACULTY_ID))
                .andExpect(jsonPath("$.name").value(FACULTY_NAME))
                .andExpect(jsonPath("$.color").value(FACULTY_COLOR));
    }

    @Test
    public void testGet_findByColor() throws Exception {
        when(facultyService.findByColor(FACULTY_COLOR)).thenReturn(List.of(faculty));

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/faculty?color=" + FACULTY_COLOR)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(FACULTY_ID))
                .andExpect(jsonPath("$[0].name").value(FACULTY_NAME))
                .andExpect(jsonPath("$[0].color").value(FACULTY_COLOR));
    }

    @Test
    public void testGet_findString() throws Exception {
        when(facultyService.findByNameOrColor(any(String.class))).thenReturn(List.of(faculty));

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/faculty/find_name_or_color?string=" + FACULTY_COLOR)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(FACULTY_ID))
                .andExpect(jsonPath("$[0].name").value(FACULTY_NAME))
                .andExpect(jsonPath("$[0].color").value(FACULTY_COLOR));
    }

    @Test
    public void testGet_getStudentsByFacultyId() throws Exception {
        when(facultyService.findById(FACULTY_ID)).thenReturn(faculty);
        when(facultyService.getStudentsByFacultyId(FACULTY_ID)).thenReturn(faculty.getStudents());

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/faculty/" + FACULTY_ID + "/students")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(ID))
                .andExpect(jsonPath("$[0].age").value(AGE))
                .andExpect(jsonPath("$[0].name").value(NAME));
    }

    @Test
    public void testPut_editFaculty() throws Exception {
        when(facultyService.updateFaculty(faculty)).thenReturn(faculty);

        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(FACULTY_ID))
                .andExpect(jsonPath("$.name").value(FACULTY_NAME))
                .andExpect(jsonPath("$.color").value(FACULTY_COLOR));
    }

    @Test
    public void testDelete_deleteFaculty() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/faculty/" + FACULTY_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
