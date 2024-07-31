package org.scaler.my_blog.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="comments")
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String body;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id",nullable=false)
    private Post post;
}
