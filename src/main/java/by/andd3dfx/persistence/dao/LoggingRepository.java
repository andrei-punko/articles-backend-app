package by.andd3dfx.persistence.dao;

import by.andd3dfx.persistence.entities.LoggedRecord;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggingRepository extends PagingAndSortingRepository<LoggedRecord, Long>, LoggingRepositoryCustom {

}
