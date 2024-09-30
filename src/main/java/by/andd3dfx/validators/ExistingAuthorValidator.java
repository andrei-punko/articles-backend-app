package by.andd3dfx.validators;

import by.andd3dfx.dto.AuthorDto;
import by.andd3dfx.persistence.dao.AuthorRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExistingAuthorValidator implements ConstraintValidator<ExistingAuthor, AuthorDto> {

    private final AuthorRepository authorRepository;

    @Override
    public boolean isValid(AuthorDto author, ConstraintValidatorContext context) {
        return author != null && author.getId() != null &&
            authorRepository.existsById(author.getId());
    }
}
