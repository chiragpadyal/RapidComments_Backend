package com.ProdSense.ProdSense.Entitys;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;


@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true)
    private String id;
    private String message;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @JsonBackReference
    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name="parentDepartment" )
    private Comments parentComments;

    
   
    @OneToMany(mappedBy= "parentComments")
    private Set<Comments> linkedComments = new HashSet<Comments>();

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;



    @ManyToOne
    @JoinColumn(name="threadId", referencedColumnName = "threadId", nullable = false)
    private ThreadEntity thread;

    /* getters & setters */

}