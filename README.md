# GitHub Repository Stats Generator

## About the Project

The GitHub Repository Stats Generator is a web service that dynamically generates an image showcasing statistics about a GitHub repository's contributors. The image includes the top contributors, their avatars, and the number of contributions they have made to the repository.

This project is designed to be integrated into other repositories, allowing users to easily display their repository's contributor statistics using a simple HTML image tag.

**🚧Still a Work in Progress🚧**

## Technologies Used

- **Java**: Core programming language used to build the application.
- **Spring Boot**: Framework for building the RESTful web service.
- **Spring WebFlux**: Used for making non-blocking HTTP requests to the GitHub API.
- **Graphics2D**: Java 2D API for creating and manipulating images.
- **Maven**: Build automation tool used to manage dependencies and packaging.
- **Heroku**: Platform-as-a-service used to deploy and run the application.

## Contributions

<img alt="Contributors of the project" src="http://www.github-read.me/repo-contributions/m04josefsen/github-repository-stats?ts=TIMESTAMP_PLACEHOLDER">

To use this in your project, use one of the two examples under:

```html
![alt text](https://github-read.me/repo-contributions/{owner}/{repository}?ts=TIMESTAMP_PLACEHOLDER)

<img alt="alt text" src="https://github-read.me/repo-contributions/{owner}/{repository}?ts=TIMESTAMP_PLACEHOLDER">
```
Replace {owner} with your GitHub username or organization name, {repository} with the name of your repository.

## Commits

<img alt="Contributors of the project" src="http://www.github-read.me/repo-commits/m04josefsen/github-repository-stats?ts=TIMESTAMP_PLACEHOLDER">

To use this in your project, you can embed the image by choosing one of the two examples below:

```html
![alt text](https://github-read.me/repo-commits/{owner}/{repository}?ts=TIMESTAMP_PLACEHOLDER)

<img alt="alt text" src="https://github-read.me/repo-commits/{owner}/{repository}?ts=TIMESTAMP_PLACEHOLDER">
```
Replace {owner} with your GitHub username or organization name, {repository} with the name of your repository.
