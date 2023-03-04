package com.ProdSense.ProdSense.Entitys;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "thread_comment")
public class ThreadComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id", nullable = false)
    private User user;

    private String content;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false, updatable = false)
    private Instant createdAt;
    //  private ZonedDateTime createdAt = ZonedDateTime.now(ZoneOffset.UTC);

    @Column(name = "last_modified", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant lastModified;

    @Column(name = "likes", columnDefinition = "bigint default 0")
    private Long likes;
    @Column(name = "dislikes", columnDefinition = "bigint default 0")
    private Long dislikes;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="threadId", referencedColumnName = "threadId", nullable = false)
    private ThreadEntity thread;

    /*

    @ManyToOne
    @JoinColumn(name="threadId", referencedColumnName = "threadId", nullable = false)
    private ThreadEntity thread;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="threadId", referencedColumnName = "threadId", nullable = false)
    private ThreadEntity thread;
    */

    @Column(name = "parent_id")
    private String parentId;

/*
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<ThreadComment> children;

    @JsonBackReference
    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name="parent" )
    private ThreadComment parent;
*/

    @JsonManagedReference
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<ThreadComment> children;

    @JsonBackReference
    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name="parent" )
    private ThreadComment parent;

}

