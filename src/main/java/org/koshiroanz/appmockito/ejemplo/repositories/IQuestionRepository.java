package org.koshiroanz.appmockito.ejemplo.repositories;

import java.util.List;

public interface IQuestionRepository {
    void save(List<String> questions);
    List<String> findQuestionByExamId(Long id);
}
