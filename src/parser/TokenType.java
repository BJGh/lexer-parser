package parser;

public enum TokenType {

    LEFT_PAREN,RIGHT_PAREN,LEFT_BRACE,RIGTH_BRACE,WHITESPACE,
    ARITHMETIC,
    SC, MULDIV,ADDSUB,

    VAR, ASSIGN,EQUAL_EQUAL,GREATER,
    LESS,NOT_EQUAL,

    STRING,NUMBER,

    LOGICAL_OP,
    ELSE_KEYWORD, IF_KEYWORD,OR,
    PRINT, WHILE_KEYWORD,DO,

    //undefined
    UNDEFINED
}
