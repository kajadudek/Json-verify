package com.json_parser.parser.FileUtils;

import com.json_parser.parser.Exceptions.MissingJsonNodeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JsonDataService {
    private final JsonDataValidator jsonDataValidator;

    public boolean checkFile(MultipartFile file) throws MissingJsonNodeException, IOException {
        return jsonDataValidator.validate(file);
    }
}
