package org.koshiroanz.appmockito.ejemplo.services;

import org.koshiroanz.appmockito.ejemplo.models.Exam;

import java.util.Optional;

public interface ExamService {
    Optional<Exam> findByName(String name);
    Exam findByNameWithQuestions(String name);

    Exam save(Exam exam);
}
