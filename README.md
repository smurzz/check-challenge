# Developer Evaluation Tool

This Spring Boot webflux application streamlines the evaluation process of technical tasks submitted by Java developer applicants. IT companies can create a private GitHub repository for each applicant, which they can access with push, pull, and commit privileges. The application evaluates the tasks based on specified criteria and stores the results in a MongoDB database along with information about evaluators and tasks. It provides an efficient and user-friendly way for IT companies to evaluate Java developer applicants and select the best candidates for their teams.

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=smurzz_check-challenge&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=smurzz_check-challenge)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=smurzz_check-challenge&metric=coverage)](https://sonarcloud.io/summary/new_code?id=smurzz_check-challenge)

## Installation

### Method 1: Local Installation

To run the application locally, follow these steps:

1. Clone the repository to your local machine.
2. In a terminal, navigate to the `cd check-challenge/check-challenge-server` folder and run **mvn clean install** and **mvn spring-boot:run**. This will start the server and connect it to your local MongoDB instance.
3. In another terminal, navigate to the `cd check-challenge/check-challenge-client` folder and run **npm install** and **npm start**. This will start the frontend on http://localhost:3000.
4. Open your web browser and go to http://localhost:3000 to access the application.

### Method 2: Docker Installation

Alternatively, you can run the application using Docker. Follow these steps:

1. Make sure you have Docker installed on your machine.
2. Clone the repository to your local machine.
3. Navigate to the cloned directory: **cd check-challenge**.
4. Run the following command to start the application: **docker-compose up**.
5. Once the application is up and running, open your web browser and go to http://localhost:3000 to access the frontend.

## Usage
Once the application is running, users can perform the following actions:

* Register as a Java developer
* View and score submitted tasks as an evaluator
* View their evaluation scores as a Java developer

## Features
* Easy and streamlined process for submitting and evaluating technical tasks
* Uses specified criteria to evaluate tasks
* Stores evaluation data in a MongoDB database
* User-friendly interface for developers and evaluators

## License
This application is licensed under the MIT License.

## Acknowledgments
We would like to thank the following resources for their contributions to this project:

* Spring Boot webflux framework
* React.js
* MongoDB database
* GitHub API

## Support and Contact Information
If you have any questions or encounter any issues with this application, please contact us at murz.sophie@gmail.com.

## Version History
v1.0 (March 1, 2023): Initial release of the Java Developer Evaluation Tool.
