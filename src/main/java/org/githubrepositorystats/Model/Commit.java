package org.githubrepositorystats.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Commit {
    private String authorName;
    private String authorEmail;
    private Date date;
    private String message;
    private String url;
    private String avatarUrl;
}
