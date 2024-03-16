package com.json_parser.parser;

import com.json_parser.parser.Exceptions.MissingJsonNodeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/inputFile")
public class JsonDataController {

    private final JsonDataService dataService;
    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file uploaded");
        }

        try {
            boolean isValidJson = this.dataService.checkFile(file);
            return ResponseEntity.ok(String.valueOf(isValidJson));
        } catch (MissingJsonNodeException e) {
            return ResponseEntity.badRequest().body("Error validating JSON AWS::IAM::Role Policy file.\n" + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.unprocessableEntity().body("Error input format:\n" + e.getMessage());
        }
    }
}
