package org.githubrepositorystats;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsController {

    private final GithubService githubService;

    public StatsController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/repo-stats/{owner}/{repository}")
    public ResponseEntity<String> getRepoStats(@PathVariable String owner, @PathVariable String repository) {
        githubService.getTest(owner, repository);

        return ResponseEntity.ok().body("OK");
    }
}
