package by.andd3dfx.services;

import by.andd3dfx.dto.ArticleDto;
import java.util.List;

public interface ArticleService {

    ArticleDto create(ArticleDto articleDto);

    ArticleDto get(Long id);

    void update(Long id, ArticleDto articleDto);

    void delete(Long id);

    List<ArticleDto> getAll();
}
