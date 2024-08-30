package org.githubrepositorystats.Github;

import org.githubrepositorystats.Model.Commit;
import org.githubrepositorystats.Model.Contributor;
import org.githubrepositorystats.MakeImage;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class GithubController {

    private final GithubService githubService;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    // Data about most contributions
    @GetMapping("/repo-contributions/{owner}/{repository}")
    public ResponseEntity<InputStreamResource> getRepoContributions(
            @PathVariable String owner,
            @PathVariable String repository,
            @RequestParam(value = "ts", required = false) String timestamp) throws IOException {
        List<Contributor> contributorList = githubService.getContributors(owner, repository);

        if (contributorList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return MakeImage.createImage(contributorList);
    }

    // Data about commits
    @GetMapping("/repo-commits/{owner}/{repository}")
    public ResponseEntity<InputStreamResource> getRepoCommits(@PathVariable String owner, @PathVariable String repository) throws IOException {
        List<Commit> commitList = githubService.getCommits(owner, repository);

        return null;
    }


}
