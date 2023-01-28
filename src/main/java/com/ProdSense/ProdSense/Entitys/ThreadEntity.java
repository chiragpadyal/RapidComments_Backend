package com.ProdSense.ProdSense.Entitys;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ThreadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String threadId;
    private String origin;

    public ThreadEntity(String origin, String threadId) {
        this.origin = origin;
        this.threadId = threadId;
    }

    public ThreadEntity(String threadId) {
        this.threadId = threadId;
    }

    @JsonBackReference
    @OneToMany(mappedBy="thread")
    private Set<Comments> threads = new HashSet<Comments>();

}
