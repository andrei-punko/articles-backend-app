package by.andd3dfx.services;

import by.andd3dfx.dto.AuthorDto;
import java.util.List;

public interface AuthorService {

    AuthorDto get(Long id);

    List<AuthorDto> getAll();
}
