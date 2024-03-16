package com.json_parser.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/inputFile")
public class JsonDataController {

    private final JsonDataService dataService;
    @PostMapping
    public ResponseEntity<Boolean> uploadFile(@RequestPart("file") MultipartFile file) {
        boolean isValidJson = this.dataService.checkFile(file);
        System.out.println(isValidJson);
        return ResponseEntity.ok(isValidJson);
    }
}
