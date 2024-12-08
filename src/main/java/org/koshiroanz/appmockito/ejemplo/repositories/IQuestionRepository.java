package org.koshiroanz.appmockito.ejemplo.repositories;

import java.util.List;

public interface IQuestionRepository {
    List<String> findQuestionByExamId(Long id);
}
