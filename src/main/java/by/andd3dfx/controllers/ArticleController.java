package by.andd3dfx.controllers;

import by.andd3dfx.dto.ArticleDto;
import by.andd3dfx.services.ArticleService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ArticleDto createArticle(@RequestBody ArticleDto newArticleDto) {
        return articleService.create(newArticleDto);
    }

    @PutMapping("/{id}")
    public ArticleDto updateArticle(@PathVariable Long id, @RequestBody ArticleDto updatedArticleDto) {
        return articleService.update(id, updatedArticleDto);
    }

    @DeleteMapping("/{id}")
    public void deleteArticle(@PathVariable Long id) {
        articleService.delete(id);
    }

    @GetMapping
    public List<ArticleDto> readArticles() {
        return articleService.getAll();
    }
}
