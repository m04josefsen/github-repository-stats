package org.githubrepositorystats.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contributor {
    private String login;
    private int contributions;
    private String avatarUrl;
}
