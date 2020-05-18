package by.andd3dfx.persistence.entities;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "logs")
public class LoggedRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "logs_id_seq")
    @SequenceGenerator(name = "logs_id_seq", sequenceName = "logs_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "method_name")
    private String methodName;

    @Column(name = "args")
    private String arguments;

    @Column
    private String result;

    @Column(name = "result_type")
    private String resultType;

    @Column(name = "ts")
    private LocalDateTime timestamp;

    @Column(name = "is_succeed")
    private Boolean isSucceed;
}
