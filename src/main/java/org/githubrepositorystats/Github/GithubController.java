package org.githubrepositorystats.Github;

import org.githubrepositorystats.Commit.Commit;
import org.githubrepositorystats.Contributor.Contributor;
import org.githubrepositorystats.Commit.CommitService;
import org.githubrepositorystats.Contributor.ContributorService;
import org.githubrepositorystats.Heatmap.HeatmapService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
public class GithubController {

    private final GithubService githubService;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    //Data about most contributions
    @GetMapping("/repo-contributions/{owner}/{repository}")
    public ResponseEntity<InputStreamResource> getRepoContributions(
            @PathVariable String owner,
            @PathVariable String repository,
            @RequestParam(required = false) String ts) throws IOException {

        List<Contributor> contributorList = githubService.getContributors(owner, repository);

        if(contributorList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String htmlContent = ContributorService.generateHtmlForContributors(contributorList);

        // Converting HTML -> image
        BufferedImage image = ImageService.htmlToImage(htmlContent);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());

        // Setting Headers
        String uniqueTs = ts != null ? ts : UUID.randomUUID().toString();
        HttpHeaders headers = new HttpHeaders();
        ImageService.setHeaders(headers, uniqueTs);

        return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);

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

        String htmlContent = CommitService.generateHtmlForCommits(commitList);

        // Converting HTML -> image
        BufferedImage image = ImageService.htmlToImage(htmlContent);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());

        // Setting Headers
        String uniqueTs = ts != null ? ts : UUID.randomUUID().toString();
        HttpHeaders headers = new HttpHeaders();
        ImageService.setHeaders(headers, uniqueTs);

        return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);
    }

    // Data about commitdates in the from of a heatmap
    @GetMapping("/repo-heatmap/{owner}/{repository}")
    public ResponseEntity<InputStreamResource> getRepoHeatmap(
            @PathVariable String owner,
            @PathVariable String repository,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String ts) throws IOException {

        //LINK FOR README
        //https://github-read.me/repo-commits/{owner}/{repository}?year=YEAR_PLACEHOLDER&ts=TIMESTAMP_PLACEHOLDER

        List<Commit> commitList = githubService.getCommits(owner, repository);

        if(commitList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        String htmlContent = HeatmapService.generateHtmlForHeatmap(commitList);

        // Converting HTML -> image
        BufferedImage image = ImageService.htmlToImage(htmlContent);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());

        // Setting Headers
        String uniqueTs = ts != null ? ts : UUID.randomUUID().toString();
        HttpHeaders headers = new HttpHeaders();
        ImageService.setHeaders(headers, uniqueTs);

        return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);
    }

    /*
    // Data about commit dates in the form of a heatmap
    @GetMapping("/repo-heatmap/{owner}/{repository}")
    public ResponseEntity<String> getRepoHeatmap(
            @PathVariable String owner,
            @PathVariable String repository,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String ts) throws IOException {

        // Fetch commits for the repository
        List<Commit> commitList = githubService.getCommits(owner, repository);

        // If no commits found, return a 404 response
        if (commitList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No commits found for the repository.");
        }

        // Generate the heatmap HTML
        String htmlContent = HeatmapService.generateHtmlForHeatmap(commitList);

        // Return the HTML content directly to be rendered in the browser
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);

        return new ResponseEntity<>(htmlContent, headers, HttpStatus.OK);
    }
     */
}
