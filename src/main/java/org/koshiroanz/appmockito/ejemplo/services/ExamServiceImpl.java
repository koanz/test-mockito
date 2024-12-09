package org.koshiroanz.appmockito.ejemplo.services;

import org.koshiroanz.appmockito.ejemplo.models.Exam;
import org.koshiroanz.appmockito.ejemplo.repositories.IExamRepository;
import org.koshiroanz.appmockito.ejemplo.repositories.IQuestionRepository;

import java.util.List;
import java.util.Optional;

public class ExamServiceImpl implements IExamService {
    private IExamRepository repository;
    private IQuestionRepository questionRepository;

    public ExamServiceImpl(IExamRepository repository, IQuestionRepository questionRepository) {
        this.repository = repository;
        this.questionRepository = questionRepository;
    }

    @Override
    public Optional<Exam> findByName(String name) {
        return repository.findAll()
                .stream()
                .filter(e -> e.getName().contains(name))
                .findFirst();
    }

    @Override
    public Exam findByNameWithQuestions(String name) {
        Optional<Exam> examOpt = this.findByName(name);
        Exam exam = null;
        if(examOpt.isPresent()) {
            exam = examOpt.orElseThrow();
            List<String> questions = questionRepository.findQuestionByExamId(exam.getId());
            exam.setQuestions(questions);
        }

        return exam;
    }

    @Override
    public Exam save(Exam exam) {
        if(!exam.getQuestions().isEmpty()) {
            this.questionRepository.save(exam.getQuestions());
        }

        return this.repository.save(exam);
    }
}
