package org.githubrepositorystats.ControllerAndService;

import org.githubrepositorystats.Model.Commit;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommitService {

    // Helper method to generate HTML for commits
    public static String generateHtmlForCommits(List<Commit> commits) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        html.append("<head><meta charset='UTF-8'/>");
        html.append("<link rel='stylesheet' href='src/main/resources/static/commit.css'/>");
        html.append("</head><body><div class='container'><h1>Commits</h1>");

        if (commits != null && !commits.isEmpty()) {
            // First commit
            Commit firstCommit = commits.get(commits.size() - 1);
            appendCommitHtml(html, firstCommit, "First Commit");

            // Last commit
            if (commits.size() > 1) {
                Commit lastCommit = commits.get(0);
                appendCommitHtml(html, lastCommit, "Latest Commit");
            }
        } else {
            html.append("<p>No commits available</p>");
        }

        html.append("</div></body></html>");
        return html.toString();
    }

    // Helper method to append commit information to the HTML
    private static void appendCommitHtml(StringBuilder html, Commit commit, String label) {
        String avatarUrl = commit.getAvatarUrl() != null ? commit.getAvatarUrl() : "default-avatar.png";
        String login = commit.getLogin() != null ? ImageService.escapeHtml(commit.getLogin()) : "Unknown";
        String message = commit.getMessage() != null ? ImageService.escapeHtml(commit.getMessage()) : "No message provided";
        String date = commit.getDate() != null ? commit.getDate().toString() : "Unknown date";

        html.append("<div class='commit'>")
                .append("<h2>").append(label).append("</h2>")
                .append("<img class='commit-avatar' src='").append(avatarUrl).append("' alt='Avatar'/>")
                .append("<div><p>").append(login).append("</p>")
                .append("<p>Message: ").append(message).append("</p>")
                .append("<p>Date: ").append(date).append("</p></div>")
                .append("</div>");
    }
}
