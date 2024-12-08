package org.koshiroanz.appmockito.ejemplo.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.koshiroanz.appmockito.ejemplo.models.Exam;
import org.koshiroanz.appmockito.ejemplo.repositories.IExamRepository;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExamServiceImplTest {

    @Test
    @DisplayName("Find exam by name")
    void findByName() {
        String examName = "Sistemas de Información";
        IExamRepository repository = mock(IExamRepository.class);
        IExamService service = new ExamServiceImpl(repository);

        List<Exam> exams = Arrays.asList(new Exam(2L, "Algoritmos I"),
                new Exam(3L, "Quimica Básica"),
                new Exam(5L, "Sistemas de Información"));
        when(repository.findAll()).thenReturn(exams);

        Exam exam = service.findByName(examName);

        assertNotNull(exam);
        assertNotEquals(1L, exam.getId());
        assertEquals(5L, exam.getId());
        assertEquals(examName, exam.getName());
    }

    @Test
    void findAll() {
    }
}