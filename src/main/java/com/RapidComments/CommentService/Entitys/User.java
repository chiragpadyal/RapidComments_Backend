package com.RapidComments.ProdSense.Entitys;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "userinfo")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "user_id", unique = true, nullable = false)
    private String user_id;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "username")
    private String username;
    @Column(name = "picture")
    private String picture;
    @Column(name = "status", nullable = false, columnDefinition = "boolean default false")
    private boolean status;

    public User() {
    }

    public User(String user_id) {
        this.user_id = user_id;
    }
    public User(Long id) {
        this.id = id;
    }

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<CommentsMysql> commentsMysqls;

    //@OneToMany(mappedBy="user")
    //private Set<Department> linkedDepartments = new HashSet<Department>();
}