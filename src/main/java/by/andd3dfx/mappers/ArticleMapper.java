package by.andd3dfx.mappers;

import by.andd3dfx.dto.ArticleDto;
import by.andd3dfx.dto.ArticleUpdateDto;
import by.andd3dfx.persistence.entities.Article;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ArticleMapper {

    ArticleDto toArticleDto(Article article);

    List<ArticleDto> toArticleDtoList(List<Article> articles);

    Article toArticle(ArticleDto articleDto);

    void toArticle(ArticleUpdateDto articleUpdateDto, @MappingTarget Article article);
}
