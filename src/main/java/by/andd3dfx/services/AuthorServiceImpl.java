package by.andd3dfx.services;

import static by.andd3dfx.configs.SecurityConfig.ROLE_ADMIN;
import static by.andd3dfx.configs.SecurityConfig.ROLE_USER;

import by.andd3dfx.dto.AuthorDto;
import by.andd3dfx.mappers.AuthorMapper;
import by.andd3dfx.persistence.dao.AuthorRepository;
import by.andd3dfx.services.exceptions.AuthorNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Secured({ROLE_ADMIN, ROLE_USER})
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Override
    @Cacheable(value = "authors", key = "#id")
    public AuthorDto get(Long id) {
        return authorRepository.findById(id)
            .map(authorMapper::toAuthorDto)
            .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    @Override
    @Cacheable(value = "allAuthors")
    public List<AuthorDto> getAll() {
        return StreamSupport.stream(authorRepository.findAll().spliterator(), false)
            .map(authorMapper::toAuthorDto)
            .collect(Collectors.toList());
    }
}
