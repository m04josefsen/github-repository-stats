package org.githubrepositorystats.Github;

import org.githubrepositorystats.Model.Contributor;
import org.githubrepositorystats.MakeImage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class GithubController {

    private final GithubService githubService;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    //TODO: en getMapping returnerer et bilde, en annen returnerer et annet etc

    /*
    @GetMapping("/repo-stats/{owner}/{repository}")
    public ResponseEntity<String> getRepoStats(@PathVariable String owner, @PathVariable String repository) {
        //githubService.getTest(owner, repository);
        //githubService.getCommits(owner, repository);
        githubService.getContributors(owner, repository);

        return ResponseEntity.ok().body("OK");
    }
     */

    @GetMapping("/repo-stats/{owner}/{repository}")
    public ResponseEntity<byte[]> getRepoStats(@PathVariable String owner, @PathVariable String repository) throws IOException {
        List<Contributor> contributorlist = githubService.getContributors(owner, repository);

        if (contributorlist.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        byte[] imageBytes = MakeImage.createImage(contributorlist);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(imageBytes.length);

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }


}
