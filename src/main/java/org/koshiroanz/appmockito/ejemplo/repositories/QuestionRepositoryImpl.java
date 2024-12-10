package org.koshiroanz.appmockito.ejemplo.repositories;

import org.koshiroanz.appmockito.ejemplo.Data;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class QuestionRepositoryImpl implements QuestionRepository {
    @Override
    public void save(List<String> questions) {
        System.out.println("QuestionRepositoryImpl.save()");

    }

    @Override
    public List<String> findQuestionByExamId(Long id) {
        System.out.println("QuestionRepositoryImpl.findQuestionByExamId()");
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Data.QUESTIONS;
    }
}
