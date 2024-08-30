package org.githubrepositorystats.Github;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.githubrepositorystats.Model.Commit;
import org.githubrepositorystats.Model.Contributor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
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

    // Method to fetch all commits in a Github repository
    public List<Commit> getCommits(String owner, String repository) {
        List<Commit> commitList = new ArrayList<>();

        String jsonResponse = this.webClient.get()
                .uri("/repos/{owner}/{repository}/commits", owner, repository)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(jsonResponse);

        return commitList;
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
                JsonNode contributor = contributors.next();
                String login = contributor.get("login").asText();
                int contributions = contributor.get("contributions").asInt();
                String avatarUrl = contributor.get("avatar_url").asText();

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
}
