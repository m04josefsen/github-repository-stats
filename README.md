# GitHub Repository Stats Generator

## About the Project

The GitHub Repository Stats Generator is a web service that dynamically generates an image showcasing statistics about a GitHub repository's contributors. The image includes the top contributors, their avatars, and the number of contributions they have made to the repository.

This project is designed to be integrated into other repositories, allowing users to easily display their repository's contributor statistics using a simple HTML image tag.

## Technologies Used

- **Java**: Core programming language used to build the application.
- **Spring Boot**: Framework for building the RESTful web service.
- **Spring WebFlux**: Used for making non-blocking HTTP requests to the GitHub API.
- **Graphics2D**: Java 2D API for creating and manipulating images.
- **Maven**: Build automation tool used to manage dependencies and packaging.

## Features

- Fetches data directly from the GitHub API.
- Dynamically generates an image with the top contributors, including their avatars and contribution count.
- Easy to integrate into any GitHub repository's README.

## Showcase

Below is a sample output of the generated image:

<img alt="Contributors of the project" src="http://localhost:8080/repo-stats/m04josefsen/github-repository-stats">

_**Note:** The image is currently only connected to localhost, and will therefore only be on at certain times._

## Usage

To use this service in your GitHub repository, add the following HTML tag to your README:

```html
<img src="http://your-server-url/repo-stats/{owner}/{repository}" alt="Repository Stats">
```
_**Note:** Currently no server to have it up at all times, usage will be updated when online._

---

*README created by ChatGPT*
