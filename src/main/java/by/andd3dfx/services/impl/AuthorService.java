package by.andd3dfx.services.impl;

import by.andd3dfx.dto.AuthorDto;
import by.andd3dfx.mappers.AuthorMapper;
import by.andd3dfx.persistence.dao.AuthorRepository;
import by.andd3dfx.services.IAuthorService;
import by.andd3dfx.exceptions.AuthorNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthorService implements IAuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Transactional(readOnly = true)
    @Override
    public AuthorDto get(Long id) {
        return authorRepository.findById(id)
            .map(authorMapper::toAuthorDto)
            .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<AuthorDto> getAll() {
        return StreamSupport.stream(authorRepository.findAll().spliterator(), false)
            .map(authorMapper::toAuthorDto)
            .toList();
    }
}
