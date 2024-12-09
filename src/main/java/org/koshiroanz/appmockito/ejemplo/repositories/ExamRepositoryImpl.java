package org.koshiroanz.appmockito.ejemplo.repositories;

import org.koshiroanz.appmockito.ejemplo.models.Exam;
import org.koshiroanz.appmockito.ejemplo.Data;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamRepositoryImpl implements ExamRepository {
    @Override
    public Exam save(Exam exam) {
        System.out.println("ExamRepositoryImpl.save()");
        return Data.EXAM;
    }

    @Override
    public List<Exam> findAll() {
        System.out.println("ExamRepositoryImpl.findAll()");

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return Data.EXAMS;
    }
}
