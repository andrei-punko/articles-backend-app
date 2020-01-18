package by.andd3dfx.persistence.entities;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "ARTICLE")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ARTICLE_ID_SEQ")
    @SequenceGenerator(name = "ARTICLE_ID_SEQ", sequenceName = "ARTICLE_ID_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "SUMMARY")
    private String summary;

    @Column(name = "TEXT")
    private String text;

    @ManyToOne
    @JoinColumn(name = "AUTHOR_ID", nullable = false, updatable = false)
    private Author author;

    @Column(name = "DATE_CREATED")
    private LocalDateTime dateCreated;

    @Column(name = "DATE_UPDATED")
    private LocalDateTime dateUpdated;
}
