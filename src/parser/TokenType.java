package parser;

public enum TokenType {
    TAG_OPEN,        // <
    TAG_END_OPEN,    // </
    TAG_CLOSE,       // >
    SELF_CLOSE,      // />
    IDENTIFIER,
    EQUALS,          // =
    STRING,          // quoted attribute value
    TEXT,            // text content between tags
    COLON,           // : (namespace prefix separator)
    EOF
}
