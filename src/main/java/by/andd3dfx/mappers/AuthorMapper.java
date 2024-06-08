package by.andd3dfx.mappers;

import by.andd3dfx.dto.AuthorDto;
import by.andd3dfx.persistence.entities.Author;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AuthorMapper {

    AuthorDto toAuthorDto(Author author);

    List<AuthorDto> toAuthorDtoList(List<Author> authors);

    Author toAuthor(AuthorDto authorDto);
}
