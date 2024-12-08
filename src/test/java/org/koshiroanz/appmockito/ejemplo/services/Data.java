package org.koshiroanz.appmockito.ejemplo.services;

import org.koshiroanz.appmockito.ejemplo.models.Exam;

import java.util.Arrays;
import java.util.List;

public class Data {
    static final List<Exam> EXAMS = Arrays.asList(new Exam(2L, "Matemáticas"),
            new Exam(3L, "Quimica Básica"),
            new Exam(5L, "Sistemas de Información"));

    static final List<String> QUESTIONS = Arrays.asList("aritmética", "integrales", "derivadas",
            "trigonometría", "geometría");
}
