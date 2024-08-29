package org.githubrepositorystats;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GithubService {

    private final WebClient webClient;

    public GithubService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.github.com").build();
    }

    public String getTest(String owner, String repository) {
        System.out.println(this.webClient.get().uri("/repos/{owner}/{repository}", owner, repository).retrieve().bodyToMono(String.class).block());

        return this.webClient.get().uri("/repos/{owner}/{repository}", owner, repository).retrieve().bodyToMono(String.class).block();
    }

    public void getCommitsTest(String owner, String repository) {
        String jsonResponse = this.webClient.get()
                .uri("/repos/{owner}/{repository}/commits", owner, repository)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(jsonResponse);
    }

    public void getContributorsTest(String owner, String repository) {
        String jsonResponse = this.webClient.get()
                .uri("/repos/{owner}/{repository}/contributors", owner, repository)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        //System.out.println(jsonResponse);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            //TODO: må egt være noe løkke
            String user = rootNode.get(0).get("login").asText();
            String contributionCount = rootNode.get(0).get("contributions").asText();

            System.out.println("Number of contributions from " + user + ": " + contributionCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
