package org.githubrepositorystats.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Commit {
    private String authorName;
    private String authorEmail;
    private String login;
    private LocalDate date;
    private String message;
    private String url;
    private String avatarUrl;
}
