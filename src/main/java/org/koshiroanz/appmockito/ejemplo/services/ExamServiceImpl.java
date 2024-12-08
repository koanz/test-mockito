package org.koshiroanz.appmockito.ejemplo.services;

import org.koshiroanz.appmockito.ejemplo.models.Exam;
import org.koshiroanz.appmockito.ejemplo.repositories.IExamRepository;

import java.util.Optional;

public class ExamServiceImpl implements IExamService {
    private IExamRepository repository;

    public ExamServiceImpl(IExamRepository repository) {
        this.repository = repository;
    }

    @Override
    public Exam findByName(String name) {
        Optional<Exam> examOpt = repository.findAll()
                .stream()
                .filter(e -> e.getName().contains(name))
                .findFirst();

        Exam exam = null;
        if(examOpt.isPresent()) {
            exam = examOpt.orElseThrow();
        }

        return exam;
    }
}
