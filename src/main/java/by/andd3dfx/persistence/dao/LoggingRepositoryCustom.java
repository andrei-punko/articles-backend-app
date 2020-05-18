package by.andd3dfx.persistence.dao;

import by.andd3dfx.dto.LoggingSearchCriteria;
import by.andd3dfx.persistence.entities.LoggedRecord;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggingRepositoryCustom {

    List<LoggedRecord> findByCriteria(LoggingSearchCriteria criteria);
}
