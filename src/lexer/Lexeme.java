package lexer;

import parser.Terminal;

public class Lexeme {
    private final Terminal terminal;

    private  String value;

    public Lexeme(Terminal terminal, String value)
    {
        this.terminal = terminal;
        this.value = value;
    }

    public Lexeme setValue(String value) {
        this.value = value;
        return null;
    }
    public Terminal getTerminal()
    {
        return terminal;
    }
    public String getValue()
    {
        return value;
    }
}
