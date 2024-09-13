package org.githubrepositorystats.ControllerAndService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.githubrepositorystats.Model.Commit;
import org.githubrepositorystats.Model.Contributor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

@Service
public class GithubService {
    private final Logger logger = Logger.getLogger(GithubService.class.getName());
    private final WebClient webClient;

    public GithubService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.github.com").build();
    }

    public void getTest(String owner, String repository) {
        System.out.println(this.webClient.get().uri("/repos/{owner}/{repository}", owner, repository).retrieve().bodyToMono(String.class).block());
    }

    // Method to fetch all contributors in a Github repository
    public List<Contributor> getContributors(String owner, String repository) {
        List<Contributor> contributorlist = new ArrayList<>();

        String jsonResponse = this.webClient.get()
                .uri("/repos/{owner}/{repository}/contributors", owner, repository)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        //System.out.println(jsonResponse);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            // Iterate over each contributor
            Iterator<JsonNode> contributors = rootNode.elements();
            while (contributors.hasNext()) {
                JsonNode contributorNode = contributors.next();

                String login = contributorNode.get("login").asText();
                int contributions = contributorNode.get("contributions").asInt();
                String avatarUrl = contributorNode.get("avatar_url").asText();

                Contributor c = new Contributor(login, contributions, avatarUrl);
                System.out.println(c);
                System.out.println();
                contributorlist.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("Error in getContributors: " + e.getMessage());
        }

        contributorlist.sort(Comparator.comparingInt(Contributor::getContributions).reversed());

        return contributorlist;
    }

    // Method to fetch all commits in a Github repository
    public List<Commit> getCommits(String owner, String repository) {
        List<Commit> commitList = new ArrayList<>();

        String jsonResponse = this.webClient.get()
                .uri("/repos/{owner}/{repository}/commits", owner, repository)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        //System.out.println(jsonResponse);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            // Iterate over each contributor
            Iterator<JsonNode> commits = rootNode.elements();
            while (commits.hasNext()) {
                JsonNode commitNode = commits.next();

                // Extracting author details
                JsonNode authorNode = commitNode.get("commit").get("author");
                String authorName = authorNode.get("name").asText();
                String authorEmail = authorNode.get("email").asText();


                // Extracting commit date
                String dateString = authorNode.get("date").asText();
                LocalDate date = null;
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    date = LocalDate.parse(dateString, formatter);  // Correct method for parsing to LocalDate
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String message = commitNode.get("commit").get("message").asText();
                String url = commitNode.get("html_url").asText();

                JsonNode authorDetailnode = commitNode.get("author");
                String avatarUrl = authorDetailnode.get("avatar_url").asText();
                String login = authorDetailnode.get("login").asText();

                Commit c = new Commit(authorName, authorEmail, login, date, message, url, avatarUrl);

                System.out.println(c);
                System.out.println();
                commitList.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("Error in getCommits: " + e.getMessage());
        }

        commitList.sort(Comparator.comparing(Commit::getDate).reversed());

        return commitList;
    }
}
