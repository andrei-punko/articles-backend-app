package by.andd3dfx.mappers;

import by.andd3dfx.dto.ArticleDto;
import by.andd3dfx.dto.ArticleUpdateDto;
import by.andd3dfx.persistence.entities.Article;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ArticleMapper {

    ArticleDto toArticleDto(Article article);

    List<ArticleDto> toArticleDtos(List<Article> articles);

    Article toArticle(ArticleDto articleDto);

    void toArticle(ArticleUpdateDto articleUpdateDto, @MappingTarget Article article);
}
