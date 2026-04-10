package by.andd3dfx.exceptions;

public abstract class NotFoundException extends RuntimeException {

    public NotFoundException(String name, Long id) {
        super(String.format("Could not find %s by id=%d", name, id));
    }
}
