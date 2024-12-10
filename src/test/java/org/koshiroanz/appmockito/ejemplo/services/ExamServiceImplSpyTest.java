package org.koshiroanz.appmockito.ejemplo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.koshiroanz.appmockito.ejemplo.Data;
import org.koshiroanz.appmockito.ejemplo.models.Exam;
import org.koshiroanz.appmockito.ejemplo.repositories.ExamRepository;
import org.koshiroanz.appmockito.ejemplo.repositories.ExamRepositoryImpl;
import org.koshiroanz.appmockito.ejemplo.repositories.QuestionRepository;
import org.koshiroanz.appmockito.ejemplo.repositories.QuestionRepositoryImpl;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamServiceImplSpyTest {
    @Spy
    ExamRepositoryImpl examRepository;

    @Spy
    QuestionRepositoryImpl questionRepository;

    @InjectMocks
    ExamServiceImpl service;

    @Captor
    ArgumentCaptor<Long> captor;

    @Test
    void testSpy() {
        ExamService examService = new ExamServiceImpl(examRepository, questionRepository);

        List<String> questions = Arrays.asList("aritmética");
        //when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(questions);
        doReturn(questions).when(questionRepository).findQuestionByExamId(anyLong());

        Exam exam = examService.findByNameWithQuestions("Matemáticas");
        assertEquals(1, exam.getId());
        assertEquals("Matemáticas", exam.getName());
        assertEquals(1, exam.getQuestions().size());
        assertTrue(exam.getQuestions().contains("aritmética"));

        verify(examRepository).findAll();
        verify(questionRepository).findQuestionByExamId(anyLong());
    }
}