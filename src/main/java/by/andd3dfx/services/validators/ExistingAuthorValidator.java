package by.andd3dfx.services.validators;

import by.andd3dfx.dto.AuthorDto;
import by.andd3dfx.persistence.dao.AuthorRepository;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class ExistingAuthorValidator implements ConstraintValidator<ExistingAuthor, AuthorDto> {

    private AuthorRepository authorRepository;

    public ExistingAuthorValidator(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public boolean isValid(AuthorDto author, ConstraintValidatorContext context) {
        return author != null && author.getId() != null &&
            authorRepository.existsById(author.getId());
    }
}
