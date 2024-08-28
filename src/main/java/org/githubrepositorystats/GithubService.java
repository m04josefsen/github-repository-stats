package org.githubrepositorystats;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GithubService {

    private final WebClient webClient;

    public GithubService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.github.com").build();
    }

    public void getTest(String owner, String repository) {
        System.out.println(this.webClient.get().uri("/repos/{owner}/{repository}", owner, repository).retrieve().bodyToMono(String.class).block());
    }
}
