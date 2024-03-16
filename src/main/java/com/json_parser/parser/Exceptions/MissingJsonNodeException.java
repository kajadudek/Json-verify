package com.json_parser.parser.Exceptions;

public class MissingJsonNodeException extends Exception{
    public MissingJsonNodeException(String nodeName) {
        super("Invalid JSON AWS::IAM::Role input. Missing " + nodeName + " property.");
    };
}
