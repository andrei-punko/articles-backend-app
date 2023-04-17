package by.andd3dfx.persistence.entities;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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
