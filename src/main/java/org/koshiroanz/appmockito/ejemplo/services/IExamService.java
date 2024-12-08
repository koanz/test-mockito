package org.koshiroanz.appmockito.ejemplo.services;

import org.koshiroanz.appmockito.ejemplo.models.Exam;

public interface IExamService {
    Exam findByName(String name);
}
