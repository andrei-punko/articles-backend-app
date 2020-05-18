package by.andd3dfx.services;

import by.andd3dfx.dto.AuthorDto;
import java.util.List;

public interface IAuthorService {

    AuthorDto get(Long id);

    List<AuthorDto> getAll();
}
