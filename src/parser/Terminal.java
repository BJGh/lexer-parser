package parser;

import java.util.regex.Pattern;


public class Terminal {

    private final TokenType returnTokenType;

    private final Pattern pattern;
    private final Integer priority;

    public Terminal(TokenType returnTokenType, String pattern)
    {
        this(returnTokenType,pattern,0);
    }

    public Terminal(TokenType returnTokenType, String pattern, Integer priority)
    {
         this.returnTokenType=returnTokenType;
         this.pattern = Pattern.compile(pattern);
         this.priority=priority;
    }

    public boolean matches(CharSequence charSequence)
    {
        return pattern.matcher(charSequence).matches();
    }

    public String getTokenType()
    {
        return returnTokenType.toString();
    }
    public Integer getPriority()
    {
        return priority;
    }



}
