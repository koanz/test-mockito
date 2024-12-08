package org.koshiroanz.appmockito.ejemplo.repositories;

import org.koshiroanz.appmockito.ejemplo.models.Exam;

import java.util.List;

public interface IExamRepository {
    List<Exam> findAll();
}
