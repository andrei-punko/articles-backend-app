package by.andd3dfx.services;

import by.andd3dfx.dto.ArticleDto;
import by.andd3dfx.dto.ArticleUpdateDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface IArticleService {

    ArticleDto create(ArticleDto articleDto);

    ArticleDto get(Long id);

    void update(Long id, ArticleUpdateDto articleUpdateDto);

    void delete(Long id);

    List<ArticleDto> getAll(Integer pageNo, Integer pageSize, String sortBy);

    Slice<ArticleDto> getAll(Pageable pageable);
}
