package org.koshiroanz.appmockito.ejemplo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.koshiroanz.appmockito.ejemplo.Data;
import org.koshiroanz.appmockito.ejemplo.models.Exam;
import org.koshiroanz.appmockito.ejemplo.repositories.ExamRepositoryImpl;
import org.koshiroanz.appmockito.ejemplo.repositories.ExamRepository;
import org.koshiroanz.appmockito.ejemplo.repositories.QuestionRepository;
import org.koshiroanz.appmockito.ejemplo.repositories.QuestionRepositoryImpl;
import org.mockito.*;
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
    ExamRepositoryImpl repository;

    @Mock
    QuestionRepositoryImpl questionRepository;

    @InjectMocks
    ExamServiceImpl service;

    @Captor
    ArgumentCaptor<Long> captor;

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
        when(repository.findAll()).thenReturn(Data.EXAMS_NULL_ID);
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
        //verify(questionRepository).findQuestionByExamId(argThat(arg -> arg != null && arg.equals(1L)));
        verify(questionRepository).findQuestionByExamId(eq(1L));
    }

    @Test
    void testArgumentMatcherWithCustomArgMatcher() {
        when(repository.findAll()).thenReturn(Data.EXAMS_NEGATIVE_ID);
        when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(Data.QUESTIONS);
        service.findByNameWithQuestions("Matemáticas");

        verify(repository).findAll();
        verify(questionRepository).findQuestionByExamId(argThat(new MyArgsMatchers()));
    }

    @Test
    void testArgumentMatcherWithCustomArgMatcherLambda() {
        when(repository.findAll()).thenReturn(Data.EXAMS);
        when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(Data.QUESTIONS);
        service.findByNameWithQuestions("Matemáticas");

        verify(repository).findAll();
        verify(questionRepository).findQuestionByExamId(argThat((argument) -> argument != null && argument > 0));
    }

    public static class MyArgsMatchers implements ArgumentMatcher<Long> {

        private Long argument;

        @Override
        public boolean matches(Long argument) {
            this.argument = argument;
            return argument != null && argument > 0;
        }

        @Override
        public String toString() {
            return "Custom error message in mockito in case of failure. The value " + argument + " needs to be a positive long";
        }
    }

    @Test
    void testArgumentCapture() {
        when(repository.findAll()).thenReturn(Data.EXAMS);
        service.findByNameWithQuestions("Matemáticas");

        //ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(questionRepository).findQuestionByExamId(captor.capture());
        assertEquals(1L, captor.getValue());
    }

    @Test
    void testDoThrow() {
        Exam exam = Data.EXAM;
        exam.setQuestions(Data.QUESTIONS);
        // es invertido al when().thenThrow(SomeException.class)
        doThrow(IllegalArgumentException.class).when(questionRepository).save(anyList());

        assertThrows(IllegalArgumentException.class, () -> {
            service.save(exam);
        });
    }

    @Test
    void testDoAnswer() {
        when(repository.findAll()).thenReturn(Data.EXAMS);
        doAnswer(invocationOnMock -> {
            Long id = invocationOnMock.getArgument(0);
            return id == 1L ? Data.QUESTIONS : Collections.emptyList();
        }).when(questionRepository).findQuestionByExamId(anyLong());

        Exam exam = service.findByNameWithQuestions("Matemáticas");
        assertEquals(5, exam.getQuestions().size());
        assertTrue(exam.getQuestions().contains("geometría"));
        assertEquals(1L, exam.getId());
        assertEquals("Matemáticas", exam.getName());

        verify(questionRepository).findQuestionByExamId(anyLong());
    }

    @Test
    @DisplayName("Test save new exam with questions and doAnswer method")
    void testSaveExamWithDoAnswer() {
        // Given
        Exam newExam = Data.EXAM;
        newExam.setQuestions(Data.QUESTIONS);
        String examName = "Física";
        doAnswer(new Answer<Exam>() {
            Long sequence = 6L;
            @Override
            public Exam answer(InvocationOnMock invocationOnMock) throws Throwable {
                Exam exam = invocationOnMock.getArgument(0);
                exam.setId(sequence++);

                return exam;
            }
        }).when(repository).save(any(Exam.class));

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
    void testDoCallRealMethod() {
        when(repository.findAll()).thenReturn(Data.EXAMS);
        //when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(Data.QUESTIONS);
        doCallRealMethod().when(questionRepository).findQuestionByExamId(anyLong());

        Exam exam = service.findByNameWithQuestions("Matemáticas");
        assertEquals(1L, exam.getId());
        assertEquals("Matemáticas", exam.getName());
    }

    @Test
    void testSpy() {
        ExamRepository examRepository = spy(ExamRepositoryImpl.class);
        QuestionRepository questionRepository = spy(QuestionRepositoryImpl.class);
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