package org.scaler.my_blog.payload;

import lombok.Data;

@Data
public class PostDTO {
    private Long id;

    private String title;
    private String content;
    private String description;
}
