package com.json_parser.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class JsonDataValidator {
    private final String RESOURCE_NODE = "Resource";
    private final String POLICY_DOCUMENT_NODE = "PolicyDocument";
    private final String POLICY_NAME_NODE = "PolicyName";
    private final String STATEMENT_NODE = "Statement";


    public boolean validate(MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(file.getInputStream());

            if (isIAMRolePolicy(rootNode)) {
                return !containsAsteriskInAnyResource(rootNode);
            }

            return false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Error validating JSON file: " + e.getMessage());
            return false;
        }
    }

    private boolean containsAsteriskInAnyResource(JsonNode rootNode) throws Exception {
        JsonNode policyDocumentNode = rootNode.path(POLICY_DOCUMENT_NODE);
        JsonNode policyStatements = this.getPolicyStatements(policyDocumentNode);

        for (JsonNode statementNode : policyStatements) {
            if (containsAsterisk(statementNode)) {
                return true;
            }
        }

        return false;
    }

    private boolean containsAsterisk(JsonNode rootNode) throws Exception {
        JsonNode resourceNode = rootNode.path(RESOURCE_NODE);

        if (resourceNode.isMissingNode()) {
            JsonNode notResourceNode = rootNode.path("NotResource");
            if (notResourceNode.isMissingNode()) {
                throw new Exception("Missing Resource/NotResource node");
            }
            return false;
        }
        else if (resourceNode.isTextual()) {
            String resourceTextValue = resourceNode.textValue();
            System.out.println(resourceTextValue);
            return resourceTextValue.equals("*");
        }
        return false;
    }

    private JsonNode getPolicyStatements(JsonNode policyDocumentNode) throws Exception {
        JsonNode statementsArrayNode = policyDocumentNode.path(STATEMENT_NODE);

        if (statementsArrayNode.isMissingNode() || statementsArrayNode.size() == 0){
            throw new Exception("No statements in this policy");
        }

        return statementsArrayNode;
    }

    private boolean isIAMRolePolicy(JsonNode rootNode) throws Exception {
        JsonNode policyDocumentNode = rootNode.path(POLICY_DOCUMENT_NODE);
        JsonNode policyNameNode = rootNode.path(POLICY_NAME_NODE);

        if (policyDocumentNode.isMissingNode()) {
            throw new Exception("Missing PolicyDocument property. Invalid JSON");
        } else if (policyNameNode.isMissingNode()) {
            throw new Exception("Missing PolicyName property. Invalid JSON");
        }

        return true;
    }
}
