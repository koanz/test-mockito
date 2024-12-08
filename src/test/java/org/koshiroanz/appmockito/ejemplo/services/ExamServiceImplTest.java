package org.koshiroanz.appmockito.ejemplo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.koshiroanz.appmockito.ejemplo.models.Exam;
import org.koshiroanz.appmockito.ejemplo.repositories.ExamRepositoryAn;
import org.koshiroanz.appmockito.ejemplo.repositories.IExamRepository;
import org.koshiroanz.appmockito.ejemplo.repositories.IQuestionRepository;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ExamServiceImplTest {
    IExamRepository repository;
    IExamService service;
    IQuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        repository = mock(ExamRepositoryAn.class);
        questionRepository = mock(IQuestionRepository.class);
        service = new ExamServiceImpl(repository, questionRepository);
    }

    @Test
    @DisplayName("Test Find exam by name.")
    void testFindByName() {
        String examName = "Sistemas de Información";

        when(repository.findAll()).thenReturn(Data.EXAMS);

        Optional<Exam> exam = service.findByName(examName);

        assertTrue(exam.isPresent());
        assertNotEquals(1L, exam.orElseThrow().getId());
        assertEquals(5L, exam.orElseThrow().getId());
        assertEquals(examName, exam.orElseThrow().getName());
    }

    @Test
    @DisplayName("Test Find exam by name from an empty list.")
    void testFindByNameEmptyList() {
        String examName = "Sistemas de Información";

        List<Exam> exams = Collections.emptyList();
        when(repository.findAll()).thenReturn(exams);

        Optional<Exam> exam = service.findByName(examName);

        assertFalse(exam.isPresent());
    }

    @Test
    @DisplayName("Test Find exam by name and verify if the size are equals (existing questions list)")
    void testQuestionsName() {
        String examName = "Sistemas de Información";
        when(repository.findAll()).thenReturn(Data.EXAMS);
        when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(Data.QUESTIONS);

        Exam exam = service.findByNameWithQuestions(examName);
        assertEquals(5, exam.getQuestions().size());
        assertTrue(exam.getQuestions().contains("aritmética"));
    }
}