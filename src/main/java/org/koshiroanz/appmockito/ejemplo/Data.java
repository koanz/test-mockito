package org.koshiroanz.appmockito.ejemplo;

import org.koshiroanz.appmockito.ejemplo.models.Exam;

import java.util.Arrays;
import java.util.List;

public class Data {
    public final static List<Exam> EXAMS = Arrays.asList(new Exam(1L, "Matemáticas"),
            new Exam(2L, "Quimica Básica"),
            new Exam(3L, "Sistemas de Información"));

    public final static List<Exam> EXAMS_NULL_ID = Arrays.asList(new Exam(null, "Matemáticas"),
            new Exam(null, "Quimica Básica"),
            new Exam(null, "Sistemas de Información"));

    public final static List<Exam> EXAMS_NEGATIVE_ID = Arrays.asList(new Exam(-1L, "Matemáticas"),
            new Exam(-2L, "Quimica Básica"),
            new Exam(null, "Sistemas de Información"));

    public final static List<String> QUESTIONS = Arrays.asList("aritmética", "integrales", "derivadas",
            "trigonometría", "geometría");

    public final static Exam EXAM = new Exam(null, "Física");
}
