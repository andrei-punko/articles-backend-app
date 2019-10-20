package by.andd3dfx.persistence.dao;

import by.andd3dfx.persistence.entities.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {

}
