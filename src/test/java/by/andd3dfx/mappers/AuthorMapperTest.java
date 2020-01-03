package by.andd3dfx.mappers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import by.andd3dfx.dto.AuthorDto;
import by.andd3dfx.persistence.entities.Author;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class AuthorMapperTest {

    private AuthorMapper mapper = Mappers.getMapper(AuthorMapper.class);

    @Test
    void toAuthorDto() {
        Author author = buildAuthor();

        AuthorDto authorDto = mapper.toAuthorDto(author);
        checkCompareAssertions(authorDto, author);
    }

    @Test
    void toAuthorDtoForNull() {
        assertThat(mapper.toAuthorDto(null), nullValue());
    }

    @Test
    void toAuthorDtoList() {
        Author author = buildAuthor();
        List<Author> authors = Arrays.asList(author);

        List<AuthorDto> authorDtoItems = mapper.toAuthorDtoList(authors);
        assertThat("Wrong result list size", authorDtoItems.size(), is(1));
        checkCompareAssertions(authorDtoItems.get(0), author);
    }

    @Test
    void toArticleDtoListForNull() {
        assertThat(mapper.toAuthorDtoList(null), nullValue());
    }

    @Test
    void toAuthor() {
        AuthorDto authorDto = buildAuthorDto();

        Author author = mapper.toAuthor(authorDto);
        checkCompareAssertions(authorDto, author);
    }

    @Test
    void toAuthorForNull() {
        assertThat(mapper.toAuthor(null), nullValue());
    }

    private Author buildAuthor() {
        Author author = new Author();
        author.setId(123L);
        author.setFirstName("Isaac");
        author.setLastName("Sirin");
        return author;
    }

    private AuthorDto buildAuthorDto() {
        AuthorDto result = new AuthorDto();
        result.setId(123L);
        result.setFirstName("Isaac");
        result.setLastName("Sirin");
        return result;
    }

    private void checkCompareAssertions(AuthorDto authorDto, Author author) {
        assertThat("Wrong id", authorDto.getId(), is(author.getId()));
        assertThat("Wrong firstName", authorDto.getFirstName(), is(author.getFirstName()));
        assertThat("Wrong lastName", authorDto.getLastName(), is(author.getLastName()));
    }
}
