package com.json_parser.parser;

import com.json_parser.parser.Exceptions.MissingJsonNodeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonDataValidatorTest {
    private JsonDataValidator jsonDataValidator;

    @BeforeEach
    public void setUp() {
        jsonDataValidator = new JsonDataValidator();
    }

    @Test
    public void validJsonFile_NoAsteriskInAnyResource() throws IOException, MissingJsonNodeException {
        // Arrange
        String jsonContent =
                "{ \"PolicyDocument\": " +
                        "{ \"Statement\": [{ \"Resource\": \"example-resource\" }] }," +
                " \"PolicyName\": " +
                        "\"example-policy\" }";
        MultipartFile file = new MockMultipartFile("file",
                "test.json",
                "application/json",
                jsonContent.getBytes());

        // Act
        boolean isValid = jsonDataValidator.validate(file);

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void validJsonFile_ResourceContainsAsterisk() throws IOException, MissingJsonNodeException {
        // Arrange
        String jsonContent =
                "{ \"PolicyDocument\": " +
                        "{ \"Statement\": [{ \"Resource\": \"*\" }] }," +
                " \"PolicyName\": " +
                        "\"example-policy\" }";
        MultipartFile file = new MockMultipartFile("file",
                "test.json",
                "application/json",
                jsonContent.getBytes());

        // Act
        boolean isValid = jsonDataValidator.validate(file);

        // Assert
        assertFalse(isValid);
    }

    @Test
    public void shouldFindSingleAsterisk_MultiplePolicyStatements() throws MissingJsonNodeException, IOException {
        // Arrange
        String jsonContent =
                "{ \"PolicyDocument\": " +
                        "{ \"Statement\": [" +
                            "{ \"Resource\": \"example-resource\" }," +
                            "{ \"Resource\": \"***\" }," +
                            "{ \"Resource\": \"example-resource\" }," +
                            "{ \"Resource\": \"*\" } ] }," +
                " \"PolicyName\": " +
                        "\"example-policy\" }";
        MultipartFile file = new MockMultipartFile("file",
                "test.json",
                "application/json",
                jsonContent.getBytes());

        // Act
        boolean isValid = jsonDataValidator.validate(file);

        // Assert
        assertFalse(isValid);
    }

    @Test
    public void shouldNotFindAsterisk() throws MissingJsonNodeException, IOException {
        // Arrange
        String jsonContent =
                "{ \"PolicyDocument\": " +
                        "{ \"Statement\": [" +
                        "{ \"Resource\": \"example*resource\" }," +
                        "{ \"Resource\": \"***\" }," +
                        "{ \"Resource\": \"* example-resource *\" }," +
                        "{ \"Resource\": \"e*xample * res*urce*\" } ] }," +
                " \"PolicyName\": " +
                        "\"example-policy\" }," +
                " \"Resource\": " +
                        "\"*\" }";
        MultipartFile file = new MockMultipartFile("file",
                "test.json",
                "application/json",
                jsonContent.getBytes());

        // Act
        boolean isValid = jsonDataValidator.validate(file);

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void invalidInputFormat() {
        // Arrange
        String fileContent = "example invalid input file";
        MultipartFile file = new MockMultipartFile("file",
                "test",
                "invalidContentType",
                fileContent.getBytes());

        // Assert
        assertThrows(IOException.class, () -> jsonDataValidator.validate(file));
    }

    @Test
    public void invalidIAMRolePolicyJson_MissingPolicyNodes() {
        // Arrange
        String jsonContent_withoutPolicyName = "{ \"PolicyDocument\": { \"Statement\": [{ \"Resource\": \"*\" }] } }";
        String jsonContent_withoutPolicyDocument = "{ \"PolicyName\": \"example-policy\" }";
        String jsonContent_withoutPolicyStatement = "{ \"PolicyDocument\": \"example-policy\", \"PolicyName\": \"example-policy\" }";
        String jsonContent_withoutStatementResource = "{ \"PolicyDocument\": { \"Statement\": [] },  \"PolicyName\": \"example-policy\" }";

        MultipartFile file_withoutPolicyName =
                new MockMultipartFile("file",
                        "test.json",
                        "application/json",
                        jsonContent_withoutPolicyName.getBytes());
        MultipartFile file_withoutPolicyDocument =
                new MockMultipartFile("file",
                        "test.json",
                        "application/json",
                        jsonContent_withoutPolicyDocument.getBytes());
        MultipartFile file_withoutPolicyStatement =
                new MockMultipartFile("file",
                        "test.json",
                        "application/json",
                        jsonContent_withoutPolicyStatement.getBytes());
        MultipartFile file_withoutStatementResource =
                new MockMultipartFile("file",
                        "test.json",
                        "application/json",
                        jsonContent_withoutStatementResource.getBytes());

        // Assert
        assertThrows(MissingJsonNodeException.class, () -> jsonDataValidator.validate(file_withoutPolicyName));
        assertThrows(MissingJsonNodeException.class, () -> jsonDataValidator.validate(file_withoutPolicyDocument));
        assertThrows(MissingJsonNodeException.class, () -> jsonDataValidator.validate(file_withoutPolicyStatement));
        assertThrows(MissingJsonNodeException.class, () -> jsonDataValidator.validate(file_withoutStatementResource));
    }
}