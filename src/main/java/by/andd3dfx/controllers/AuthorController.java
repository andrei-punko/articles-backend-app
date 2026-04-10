package by.andd3dfx.controllers;

import by.andd3dfx.dto.AuthorDto;
import by.andd3dfx.services.IAuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get author by id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Author successfully retrieved",
            content = @Content(schema = @Schema(implementation = AuthorDto.class))),
        @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
        @ApiResponse(responseCode = "404", description = "Author not found"),
    })
    @GetMapping("/{id}")
    public AuthorDto readAuthor(
        @Parameter(description = "Author's id")
        @NotNull @PathVariable("id") Long id) {
        return authorService.get(id);
    }

    @Operation(summary = "Get all authors")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Authors successfully retrieved",
            content = @Content(schema = @Schema(implementation = AuthorDto.class))),
        @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource")
    })
    @GetMapping
    public List<AuthorDto> readAuthors() {
        return authorService.getAll();
    }
}
