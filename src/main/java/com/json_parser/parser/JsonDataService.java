package com.json_parser.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class JsonDataService {
    private final JsonDataValidator jsonDataValidator;
    public boolean checkFile(MultipartFile file) {
        return jsonDataValidator.validate(file);
    }
}
