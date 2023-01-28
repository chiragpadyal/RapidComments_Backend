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

}
