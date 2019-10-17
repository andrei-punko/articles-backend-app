package by.andd3dfx.services.exceptions;

public class ArticleNotFoundException extends RuntimeException {

    public ArticleNotFoundException(Long id) {
        super("Could not find an article by id=" + id);
    }
}
