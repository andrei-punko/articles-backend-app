package by.andd3dfx.services;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import by.andd3dfx.dto.AuthorDto;
import by.andd3dfx.mappers.AuthorMapper;
import by.andd3dfx.persistence.dao.AuthorRepository;
import by.andd3dfx.persistence.entities.Author;
import by.andd3dfx.exceptions.AuthorNotFoundException;
import by.andd3dfx.services.impl.AuthorService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepositoryMock;

    @Mock
    private AuthorMapper authorMapperMock;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void get() {
        final Long AUTHOR_ID = 123L;
        final Author author = new Author();
        final Optional<Author> optionalAuthor = Optional.of(author);
        final AuthorDto authorDto = new AuthorDto();
        Mockito.when(authorRepositoryMock.findById(AUTHOR_ID)).thenReturn(optionalAuthor);
        Mockito.when(authorMapperMock.toAuthorDto(author)).thenReturn(authorDto);

        AuthorDto result = authorService.get(AUTHOR_ID);

        Mockito.verify(authorRepositoryMock).findById(AUTHOR_ID);
        Mockito.verify(authorMapperMock).toAuthorDto(author);
        assertThat(result, is(authorDto));
    }

    @Test
    public void getAbsentAuthor() {
        final Long AUTHOR_ID = 123L;
        final Optional<Author> optionalAuthor = Optional.empty();
        Mockito.when(authorRepositoryMock.findById(AUTHOR_ID)).thenReturn(optionalAuthor);

        try {
            authorService.get(AUTHOR_ID);

            fail("Exception should be thrown");
        } catch (AuthorNotFoundException ex) {
            Mockito.verify(authorRepositoryMock).findById(AUTHOR_ID);
        }
    }

    @Test
    void getAll() {
        final Author author = new Author();
        final AuthorDto authorDto = new AuthorDto();
        final Iterable<Author> authors = Arrays.asList(author);
        final List<AuthorDto> authorDtoList = Arrays.asList(authorDto);
        Mockito.when(authorRepositoryMock.findAll()).thenReturn(authors);
        Mockito.when(authorMapperMock.toAuthorDto(author)).thenReturn(authorDto);

        List<AuthorDto> result = authorService.getAll();

        Mockito.verify(authorRepositoryMock).findAll();
        Mockito.verify(authorMapperMock).toAuthorDto(author);
        assertThat(result, is(authorDtoList));
    }
}
