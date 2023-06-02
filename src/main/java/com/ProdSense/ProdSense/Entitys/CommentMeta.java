package com.ProdSense.ProdSense.Entitys;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class CommentMeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    * 2 - no vote
    * 0 - dislike
    * 1 - like
    */
    private Long vote;
    private  String report;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="comment_id", referencedColumnName = "id", nullable = false)
    private ThreadComment comment;

}
