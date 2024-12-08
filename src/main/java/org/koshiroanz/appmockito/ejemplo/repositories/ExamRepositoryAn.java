package org.koshiroanz.appmockito.ejemplo.repositories;

import org.koshiroanz.appmockito.ejemplo.models.Exam;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamRepositoryAn implements IExamRepository {
    @Override
    public List<Exam> findAll() {
        try {
            System.out.println("Find All");
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
