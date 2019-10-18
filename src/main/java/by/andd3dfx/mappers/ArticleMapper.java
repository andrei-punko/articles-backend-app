package by.andd3dfx.mappers;

import by.andd3dfx.dto.ArticleDto;
import by.andd3dfx.persistence.entities.Article;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    ArticleDto toArticleDtos(Article article);

    List<ArticleDto> toArticleDtos(List<Article> articles);

    Article toArticle(ArticleDto articleDto);
}
