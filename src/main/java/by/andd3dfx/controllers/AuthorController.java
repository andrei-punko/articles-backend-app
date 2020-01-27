package by.andd3dfx.controllers;

import by.andd3dfx.dto.AuthorDto;
import by.andd3dfx.services.AuthorService;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/{id}")
    public AuthorDto readAuthor(@NotNull @PathVariable Long id) {
        return authorService.get(id);
    }

    @GetMapping
    public List<AuthorDto> readAuthors() {
        return authorService.getAll();
    }
}