package org.githubrepositorystats.Github;

import org.githubrepositorystats.Model.Commit;
import org.githubrepositorystats.Model.Contributor;
import org.githubrepositorystats.MakeImage;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

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
            @RequestParam(required = false) String ts) throws IOException {

        List<Contributor> contributorList = githubService.getContributors(owner, repository);

        if (contributorList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Generate a unique cache-busting parameter
        String uniqueTs = ts != null ? ts : UUID.randomUUID().toString();
        HttpHeaders headers = new HttpHeaders();
        setHeaders(headers, uniqueTs);

        return MakeImage.createImage(contributorList);
    }

    // Data about commits
    @GetMapping("/repo-commits/{owner}/{repository}")
    public ResponseEntity<InputStreamResource> getRepoCommits(
            @PathVariable String owner,
            @PathVariable String repository,
            @RequestParam(required = false) String ts) throws IOException {

        List<Commit> commitList = githubService.getCommits(owner, repository);

        if(commitList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        String uniqueTs = ts != null ? ts : UUID.randomUUID().toString();
        HttpHeaders headers = new HttpHeaders();
        setHeaders(headers, uniqueTs);

        return null;
    }

    public void setHeaders(HttpHeaders headers, String uniqueTs) {
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.set("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.set("Pragma", "no-cache");
        headers.set("Expires", "0");
        headers.set("Timestamp", uniqueTs); // Optionally include this for debugging
    }


}
