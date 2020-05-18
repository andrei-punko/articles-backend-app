package by.andd3dfx.exceptions;

public class ArticleNotFoundException extends NotFoundException {

    public ArticleNotFoundException(Long id) {
        super("an article", id);
    }
}
