package by.andd3dfx.validators;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import by.andd3dfx.dto.AuthorDto;
import by.andd3dfx.persistence.dao.AuthorRepository;
import by.andd3dfx.validators.ExistingAuthorValidator;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExistingAuthorValidatorTest {

    @Mock
    private AuthorRepository authorRepositoryMock;

    @Mock
    private ConstraintValidatorContext contextMock;

    @InjectMocks
    private ExistingAuthorValidator existingAuthorValidator; // constructor injection

    @Test
    void isValidForNullAuthor() {
        assertThat(existingAuthorValidator.isValid(null, contextMock), is(false));
    }

    @Test
    void isValidForAuthorWithNullId() {
        assertThat(existingAuthorValidator.isValid(new AuthorDto(), contextMock), is(false));
    }

    @Test
    void isValidForAuthorWhichNotPresentInDb() {
        AuthorDto author = new AuthorDto();
        final long AUTHOR_ID = 21L;
        author.setId(AUTHOR_ID);
        Mockito.when(authorRepositoryMock.existsById(AUTHOR_ID)).thenReturn(false);

        assertThat(existingAuthorValidator.isValid(author, contextMock), is(false));

        Mockito.verify(authorRepositoryMock).existsById(AUTHOR_ID);
    }

    @Test
    void isValid() {
        AuthorDto author = new AuthorDto();
        final long AUTHOR_ID = 21L;
        author.setId(AUTHOR_ID);
        Mockito.when(authorRepositoryMock.existsById(AUTHOR_ID)).thenReturn(true);

        assertThat(existingAuthorValidator.isValid(author, contextMock), is(true));

        Mockito.verify(authorRepositoryMock).existsById(AUTHOR_ID);
    }
}
