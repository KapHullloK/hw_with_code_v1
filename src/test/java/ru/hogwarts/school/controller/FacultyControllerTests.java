package ru.hogwarts.school.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FacultyControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getRootUrl() {
        return "http://localhost:" + port + "/faculty";
    }

    private Long createdFacultyId;

    @BeforeEach
    void setUp() {
        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(getRootUrl(), faculty, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        createdFacultyId = response.getBody().getId();
    }

    @Test
    void testAddFaculty() {
        assertThat(createdFacultyId).isNotNull();
    }

    @Test
    void testGetFacultyById() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(getRootUrl() + "/" + createdFacultyId, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(createdFacultyId);
    }

    @Test
    void testUpdateFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Slytherin");
        faculty.setColor("Green");

        HttpEntity<Faculty> requestEntity = new HttpEntity<>(faculty);
        ResponseEntity<Faculty> response = restTemplate.exchange(
                getRootUrl() + "/" + createdFacultyId,
                HttpMethod.PUT,
                requestEntity,
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Slytherin");
        assertThat(response.getBody().getColor()).isEqualTo("Green");
    }

    @Test
    void testDeleteFaculty() {
        ResponseEntity<Faculty> response = restTemplate.exchange(
                getRootUrl() + "/" + createdFacultyId,
                HttpMethod.DELETE,
                null,
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(createdFacultyId);
    }

    @Test
    void testGetFacultyByColor() {
        String color = "Red";

        ResponseEntity<List> response = restTemplate.getForEntity(getRootUrl() + "/by-color/" + color, List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetFacultiesByNameOrColor() {
        URI uriByName = UriComponentsBuilder.fromUriString(getRootUrl())
                .queryParam("name", "Gryffindor")
                .build()
                .toUri();

        ResponseEntity<List> responseByName = restTemplate.getForEntity(uriByName, List.class);
        assertThat(responseByName.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetFacultyStudents() {
        Faculty faculty = new Faculty();
        faculty.setName("Hufflepuff");
        faculty.setColor("Yellow");

        ResponseEntity<Faculty> facultyResponse = restTemplate.postForEntity(getRootUrl(), faculty, Faculty.class);
        Long facultyId = facultyResponse.getBody().getId();

        Student student = new Student();
        student.setName("Harry");
        student.setAge(17);
        student.setFaculty(new Faculty());
        student.getFaculty().setId(facultyId);

        ResponseEntity<Student> studentResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/student",
                student,
                Student.class
        );


        URI uri = UriComponentsBuilder.fromUriString(getRootUrl() + "/students")
                .queryParam("id", facultyId)
                .build()
                .toUri();

        ResponseEntity<List> response = restTemplate.getForEntity(uri, List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}