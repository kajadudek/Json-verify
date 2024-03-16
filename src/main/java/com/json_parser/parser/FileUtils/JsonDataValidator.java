package com.json_parser.parser.FileUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.json_parser.parser.Exceptions.MissingJsonNodeException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class JsonDataValidator {
    private final String RESOURCE_NODE = "Resource";
    private final String POLICY_DOCUMENT_NODE = "PolicyDocument";
    private final String POLICY_NAME_NODE = "PolicyName";
    private final String STATEMENT_NODE = "Statement";


    public boolean validate(@NonNull MultipartFile file) throws MissingJsonNodeException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(file.getInputStream());

        if (isIAMRolePolicy(rootNode)) {
            return !containsAsteriskInAnyResource(rootNode);
        }

        return false;
    }

    private boolean containsAsteriskInAnyResource(JsonNode rootNode) throws MissingJsonNodeException {
        JsonNode policyDocumentNode = rootNode.path(POLICY_DOCUMENT_NODE);
        JsonNode policyStatements = this.getPolicyStatements(policyDocumentNode);

        for (JsonNode statementNode : policyStatements) {
            if (containsAsterisk(statementNode)) {
                return true;
            }
        }

        return false;
    }

    private boolean containsAsterisk(JsonNode rootNode) throws MissingJsonNodeException {
        JsonNode resourceNode = rootNode.path(RESOURCE_NODE);

        if (resourceNode.isMissingNode()) {
            JsonNode notResourceNode = rootNode.path("NotResource");
            if (notResourceNode.isMissingNode()) {
                throw new MissingJsonNodeException("Resource/NotResource");
            }
            return false;
        } else if (resourceNode.isTextual()) {
            String resourceTextValue = resourceNode.textValue();
            return resourceTextValue.equals("*");
        }

        return false;
    }

    private JsonNode getPolicyStatements(JsonNode policyDocumentNode) throws MissingJsonNodeException {
        JsonNode statementsArrayNode = policyDocumentNode.path(STATEMENT_NODE);

        if (statementsArrayNode.isMissingNode() || statementsArrayNode.size() == 0) {
            throw new MissingJsonNodeException("Statement");
        }

        return statementsArrayNode;
    }

    private boolean isIAMRolePolicy(JsonNode rootNode) throws MissingJsonNodeException {
        JsonNode policyDocumentNode = rootNode.path(POLICY_DOCUMENT_NODE);
        JsonNode policyNameNode = rootNode.path(POLICY_NAME_NODE);

        if (policyDocumentNode.isMissingNode()) {
            throw new MissingJsonNodeException("PolicyDocument");
        } else if (policyNameNode.isMissingNode()) {
            throw new MissingJsonNodeException("PolicyName");
        }

        return true;
    }
}
