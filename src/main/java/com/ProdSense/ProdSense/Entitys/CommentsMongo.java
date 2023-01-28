package com.ProdSense.ProdSense.Entitys;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
public class CommentsMongo {

    @Id
    public String id;

    public Long like;
    public Long dislike;

    public CommentsMongo(String id) {
        this.id = id;
        this.like = 0L;
        this.dislike = 0L;
    }
}
