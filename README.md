# JSON Verifier

SpringBoot application that checks whether a given JSON file conforms to the AWS::IAM::Role Policy format with specified restrictions for `Resource` fields.
Developed with Java for backend services and simple JavaScript for user interface.

## Prerequisites

- Java 17 or later
- Maven
- Docker (optional)

## Installation and Usage

### Clone the repository

1. Clone the repository to your local machine:

    ```
    git clone https://github.com/kajadudek/Json-verify.git
    ```

2. Navigate to the project directory:

    ```
    cd json-verifier
    ```

3. Build the project using Maven:

    ```
    mvn clean package
    ```

4. Run the application:

    ```
    java -jar target/json-verifier.jar
    ```

5. Access the application in your web browser at [http://localhost:8080](http://localhost:8080).

### Or run it using Docker

You can also run the application using Docker

1. Navigate to the project directory and build the Docker image:

    ```
    docker build -t json-verifier .
    ```

2. Run the Docker container:

    ```
    docker run -p 8080:8080 json-verifier
    ```

3. Access the application in your web browser at [http://localhost:8080](http://localhost:8080).

### Or access it on a hosted website
Note: Rendering the hosted website may take a few seconds

https://json-verifier-latest.onrender.com/

