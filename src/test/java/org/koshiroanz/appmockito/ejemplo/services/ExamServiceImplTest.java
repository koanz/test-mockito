package org.koshiroanz.appmockito.ejemplo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.koshiroanz.appmockito.ejemplo.models.Exam;
import org.koshiroanz.appmockito.ejemplo.repositories.ExamRepositoryAn;
import org.koshiroanz.appmockito.ejemplo.repositories.IExamRepository;
import org.koshiroanz.appmockito.ejemplo.repositories.IQuestionRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExamServiceImplTest {
    @Mock
    IExamRepository repository;

    @Mock
    IQuestionRepository questionRepository;

    @InjectMocks
    ExamServiceImpl service;

    @BeforeEach
    void setUp() {
        // Enable mocks annotations
//        MockitoAnnotations.openMocks(this);
//        repository = mock(ExamRepositoryAn.class);
//        questionRepository = mock(IQuestionRepository.class);
//        service = new ExamServiceImpl(repository, questionRepository);
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

    @Test
    @DisplayName("Test Find exam by name and verify if the size are equals (existing questions list) with verify()")
    void testQuestionsNameWithVerify() {
        String examName = "Sistemas de Información";
        when(repository.findAll()).thenReturn(Data.EXAMS);
        when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(Data.QUESTIONS);

        Exam exam = service.findByNameWithQuestions(examName);
        assertEquals(5, exam.getQuestions().size());
        assertTrue(exam.getQuestions().contains("aritmética"));

        verify(repository).findAll();
        verify(questionRepository).findQuestionByExamId(anyLong());
    }

    @Test
    @DisplayName("Test Find exam by unexisting exam name and verify if the size are equals (existing questions list) with verify()")
    void testUnexistingExamenWithVerify() {
        String examName = "Física";
        when(repository.findAll()).thenReturn(Collections.emptyList());
        when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(Data.QUESTIONS);

        Exam exam = service.findByNameWithQuestions(examName);
        assertNull(exam);

        verify(repository).findAll();
        verify(questionRepository).findQuestionByExamId(anyLong());
    }

    @Test
    @DisplayName("Test save new exam with questions")
    void testSaveExam() {
        // Given
        Exam newExam = Data.EXAM;
        newExam.setQuestions(Data.QUESTIONS);
        String examName = "Física";

        when(repository.save(any(Exam.class))).then(new Answer<Exam>() {
            Long sequence = 6L;
            @Override
            public Exam answer(InvocationOnMock invocationOnMock) throws Throwable {
                Exam exam = invocationOnMock.getArgument(0);
                exam.setId(sequence++);

                return exam;
            }
        });

        // When
        Exam exam = service.save(newExam);

        // Then
        assertNotNull(exam.getId());
        assertEquals(6L, exam.getId());
        assertEquals(examName, exam.getName());

        verify(repository).save(any(Exam.class));
        verify(questionRepository).save(anyList());
    }

    @Test
    void testManejoException() {
        when(repository.findAll()).thenReturn(Data.EXAMS_ID_NULL);
        when(questionRepository.findQuestionByExamId(isNull())).thenThrow(RuntimeException.class);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
           service.findByNameWithQuestions("Matemáticas");
        });
        assertEquals(IllegalArgumentException.class, exception.getClass());
        verify(repository).findAll();
        verify(questionRepository).findQuestionByExamId(isNull());
    }

    @Test
    void testArgumentMatcher() {
        when(repository.findAll()).thenReturn(Data.EXAMS);
        when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(Data.QUESTIONS);
        service.findByNameWithQuestions("Matemáticas");

        verify(repository).findAll();
        // Same thing => .findQuestionByExamId(argThat(arg -> arg.equals(1L))) = .findQuestionByExamId(1L);
        verify(questionRepository).findQuestionByExamId(argThat(arg -> arg != null && arg.equals(1L)));
    }
}