package by.andd3dfx.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import by.andd3dfx.dto.AuthorDto;
import by.andd3dfx.services.AuthorService;
import by.andd3dfx.services.exceptions.AuthorNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthorControllerTest {

    @Mock
    private AuthorService authorServiceMock;

    @InjectMocks
    private AuthorController authorController;

    @Test
    void readAuthor() {
        final Long id = 123L;
        final AuthorDto resultAuthorDto = new AuthorDto();
        Mockito.when(authorServiceMock.get(id)).thenReturn(resultAuthorDto);

        AuthorDto result = authorController.readAuthor(id);

        Mockito.verify(authorServiceMock).get(id);
        assertThat("Wrong result", result, is(resultAuthorDto));
    }

    @Test
    public void readAbsentAuthor() {
        final Long id = 123L;
        final AuthorNotFoundException authorNotFoundException = new AuthorNotFoundException(id);
        Mockito.when(authorServiceMock.get(id)).thenThrow(authorNotFoundException);

        try {
            authorController.readAuthor(id);
            fail("Exception should be thrown");
        } catch (AuthorNotFoundException e) {
            Mockito.verify(authorServiceMock).get(id);
            assertThat("Wrong exception", e, is(authorNotFoundException));
        }
    }

    @Test
    void readAuthors() {
        final List<AuthorDto> authorDtoList = new ArrayList<>();
        final List<AuthorDto> authors = new ArrayList<>();
        Mockito.when(authorServiceMock.getAll()).thenReturn(authors);

        List<AuthorDto> result = authorController.readAuthors();

        Mockito.verify(authorServiceMock).getAll();
        assertThat("Wrong result", result, is(authorDtoList));
    }
}
