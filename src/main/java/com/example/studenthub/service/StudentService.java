package com.example.studenthub.service;

import com.example.studenthub.entity.Student;
import com.example.studenthub.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Create a new student
    public Student createStudent(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Email already exists: " + student.getEmail());
        }
        return studentRepository.save(student);
    }

    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get all students with pagination and sorting
    public Page<Student> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    // Get student by ID
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    // Get student by email
    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    // Update student
    public Student updateStudent(Long id, Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        // Check if email is being changed and if new email already exists
        if (!student.getEmail().equals(studentDetails.getEmail())
                && studentRepository.existsByEmail(studentDetails.getEmail())) {
            throw new RuntimeException("Email already exists: " + studentDetails.getEmail());
        }

        student.setName(studentDetails.getName());
        student.setEmail(studentDetails.getEmail());
        student.setBatch(studentDetails.getBatch());
        student.setGpa(studentDetails.getGpa());

        return studentRepository.save(student);
    }

    // Delete student
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        studentRepository.delete(student);
    }

    // Check if student exists by ID
    public boolean existsById(Long id) {
        return studentRepository.existsById(id);
    }

    // Count total students
    public long countStudents() {
        return studentRepository.count();
    }
}
