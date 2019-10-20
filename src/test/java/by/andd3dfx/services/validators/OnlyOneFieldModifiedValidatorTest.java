package by.andd3dfx.services.validators;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import by.andd3dfx.dto.ArticleDto;
import java.lang.annotation.Annotation;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OnlyOneFieldModifiedValidatorTest {

    @Mock
    private ConstraintValidatorContext contextMock;

    @InjectMocks
    private OnlyOneFieldModifiedValidator onlyOneFieldModifiedValidator; // constructor injection

    @BeforeEach
    public void setup() {
        onlyOneFieldModifiedValidator.initialize(new OnlyOneFieldModified() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String message() {
                return null;
            }

            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public Class<? extends Payload>[] payload() {
                return new Class[0];
            }

            @Override
            public String[] fields() {
                return new String[]{"text", "title", "summary"};
            }
        });
    }

    @Test
    void isValidWhenNoFieldsChanged() {
        ArticleDto article = new ArticleDto();

        assertThat(onlyOneFieldModifiedValidator.isValid(article, contextMock), is(false));
    }

    @Test
    void isValidWhenOneFieldChanged_ForTitle() {
        ArticleDto article = new ArticleDto();
        article.setTitle("Some Title");

        assertThat(onlyOneFieldModifiedValidator.isValid(article, contextMock), is(true));
    }

    @Test
    void isValidWhenOneFieldChanged_ForSummary() {
        ArticleDto article = new ArticleDto();
        article.setSummary("Some Summary");

        assertThat(onlyOneFieldModifiedValidator.isValid(article, contextMock), is(true));
    }

    @Test
    void isValidWhenOneFieldChanged_ForText() {
        ArticleDto article = new ArticleDto();
        article.setText("Some Text");

        assertThat(onlyOneFieldModifiedValidator.isValid(article, contextMock), is(true));
    }

    @Test
    void isValidWhenTwoFieldsChanged() {
        ArticleDto article = new ArticleDto();
        article.setTitle("Some Title");
        article.setSummary("Some Summary");

        assertThat(onlyOneFieldModifiedValidator.isValid(article, contextMock), is(false));
    }

    @Test
    void isValidWhenThreeFieldsChanged() {
        ArticleDto article = new ArticleDto();
        article.setTitle("Some Title");
        article.setSummary("Some Summary");
        article.setText("Some Text");

        assertThat(onlyOneFieldModifiedValidator.isValid(article, contextMock), is(false));
    }
}
