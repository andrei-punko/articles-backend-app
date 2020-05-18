package by.andd3dfx.exceptions;

public class AuthorNotFoundException extends NotFoundException {

    public AuthorNotFoundException(Long id) {
        super("an author", id);
    }
}
