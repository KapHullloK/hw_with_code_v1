package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;


@Service
public class FacultyService {

    private FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty add(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty get(Long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty update(Long id, Faculty faculty) {
        if (facultyRepository.existsById(id)) {
            return facultyRepository.save(faculty);
        }
        return null;
    }

    public Faculty delete(Long id) {
        Faculty faculty = this.get(id);
        if (faculty != null) {
            facultyRepository.delete(faculty);
        }
        return faculty;
    }

    public List<Faculty> getByColor(String color) {
        return facultyRepository.findAllByColor(color);
    }

    public List<Faculty> getFacultiesByNameOrColor(String name, String color) {
        return facultyRepository.findAllByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    public List<Student> getStudents(Long id) {
        if (facultyRepository.existsById(id)) {
            return this.get(id).getStudents();
        }
        return null;
    }

}
