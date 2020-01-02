package by.andd3dfx.controllers;

import by.andd3dfx.dto.ArticleDto;
import by.andd3dfx.dto.ArticleUpdateDto;
import by.andd3dfx.services.ArticleService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleDto createArticle(
        @Validated
        @RequestBody ArticleDto newArticleDto
    ) {
        return articleService.create(newArticleDto);
    }

    @GetMapping("/{id}")
    public ArticleDto readArticle(
        @NotNull
        @PathVariable Long id
    ) {
        return articleService.get(id);
    }

    @PatchMapping("/{id}")
    public void updateArticle(
        @NotNull
        @PathVariable Long id,
        @Validated
        @RequestBody ArticleUpdateDto articleUpdateDto
    ) {
        articleService.update(id, articleUpdateDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArticle(
        @NotNull
        @PathVariable Long id
    ) {
        articleService.delete(id);
    }

    public List<ArticleDto> readArticles(
        @RequestParam(defaultValue = "0") Integer pageNo,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "title") String sortBy
    ) {
        Pageable pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        final Page<ArticleDto> articleDtoPage = readArticlesPaged(pageRequest);
        return articleDtoPage.getContent();
    }

    @GetMapping
    // Workaround for Swagger bug, according to https://github.com/springfox/springfox/issues/2623#issuecomment-414297583
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
            value = "Results page you want to retrieve (0..N)", defaultValue = "0"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
            value = "Number of records per page.", defaultValue = "10"),
        @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
            value = "Sorting criteria in the format: property(,asc|desc). " +
                "Default sort order is ascending. " +
                "Multiple sort criteria are supported.",
        defaultValue = "title,ASC")
    })
    public Page<ArticleDto> readArticlesPaged(
        @PageableDefault(page = 0, size = 10)
        @SortDefault.SortDefaults({
            @SortDefault(sort = "title", direction = Sort.Direction.ASC)
        }) Pageable pageable
    ) {
        return articleService.getAll(pageable);
    }
}
