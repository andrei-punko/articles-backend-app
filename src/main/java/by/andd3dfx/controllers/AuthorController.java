package by.andd3dfx.controllers;

import by.andd3dfx.dto.AuthorDto;
import by.andd3dfx.services.IAuthorService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authors")
public class AuthorController {

    private final IAuthorService authorService;

    @ApiOperation(value = "Get author by id", response = AuthorDto.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Author successfully retrieved"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 404, message = "Author not found"),
    })
    @GetMapping("/{id}")
    public AuthorDto readAuthor(
        @ApiParam("Author's id")
        @NotNull @PathVariable Long id) {
        return authorService.get(id);
    }

    @ApiOperation(value = "Get all authors", response = List.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Authors successfully retrieved"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource")
    })
    @GetMapping
    public List<AuthorDto> readAuthors() {
        return authorService.getAll();
    }
}
