package lexer;



import parser.Terminal;
import parser.TokenType;

import java.util.List;
import java.util.ArrayList;

//import java.io.IOException;

public class Lexer {
    private  static final List<Terminal> _terminals = List.of(
       new Terminal(TokenType.VAR, "^[a-zA-Z_]{1}\\w*$"),
       new Terminal(TokenType.NUMBER, "0|[1-9][0-9]*"),
       new Terminal(TokenType.ASSIGN, "=",1),
       new Terminal(TokenType.LOGICAL_OP,"==|>|<|!=",2),
       new Terminal(TokenType.WHILE_KEYWORD,"while",1),
       new Terminal(TokenType.IF_KEYWORD, "if",1),
       new Terminal(TokenType.ELSE_KEYWORD, "else",1),
       new Terminal(TokenType.DO,"do",1),
       new Terminal(TokenType.PRINT,"print"),
       new Terminal(TokenType.LEFT_PAREN,"\\("),
       new Terminal(TokenType.RIGHT_PAREN,"\\)"),
       new Terminal(TokenType.LEFT_BRACE,"\\{"),
       new Terminal(TokenType.RIGTH_BRACE,"\\}"),
       new Terminal(TokenType.SC,";"),
       new Terminal(TokenType.STRING,"^s+"),
      // new Terminal(TokenType.ARITHMETIC,"[+-/*]|\\+\\+|\\-\\-"),
             new Terminal(TokenType.MULDIV,"[*/]",4),
       new Terminal(TokenType.ADDSUB,"[+-]",3),
       new Terminal(TokenType.WHITESPACE, "\\s+")
   );





   public static List<Lexeme> getTokenList(String test) {
      StringBuilder input = new StringBuilder();
      List<Lexeme> lexemes = new ArrayList<>();

      input.append(test);
      input.append('$');
      while (input.charAt(0) !='$') {
         Lexeme lexeme = extractNextLexeme(input);
         lexemes.add(lexeme);
         if(!lexeme.getTerminal().getTokenType().equals("WHITESPACE")){lexemes.add(lexeme);}
         input.delete(0, lexeme.getValue().length());
      }
      return lexemes;
   }


   private static Lexeme extractNextLexeme(StringBuilder input) {
      StringBuilder builder = new StringBuilder();
      builder.append(input.charAt(0));
      if (terminalMatches(builder)) {
         while (terminalMatches(builder) && builder.length() != input.length()) {
            builder.append(input.charAt(builder.length()));
         }
         builder.deleteCharAt(builder.length() - 1);
         List<Terminal> terminals = lookupTerminals(builder);
         return new Lexeme(getTerminalPriority(terminals), builder.toString());
      }else{throw new RuntimeException("Unexpected symbol"+builder);}

   }

   private static boolean terminalMatches(StringBuilder buffer)
   {
      return lookupTerminals(buffer).size()!=0;
   }

   private static Terminal getTerminalPriority(List<Terminal> terminals)
   {
      Terminal terminalPriority = terminals.get(0);
      for(Terminal terminal:terminals)
      {
         if(terminal.getPriority()>terminalPriority.getPriority())
         {
            terminalPriority=terminal;
         }
      }
      return terminalPriority;
   }
   private static List<Terminal> lookupTerminals(StringBuilder buffer)
   {
      List<Terminal> terminals = new ArrayList<>();
      for (Terminal terminal: _terminals) { if(terminal.matches(buffer)){terminals.add(terminal);} }
      return terminals;
   }
   private static StringBuilder lookupInput(String[] args)
   {
      if(args.length==0){throw new IllegalArgumentException("Input string not found");}
      StringBuilder buff = new StringBuilder();
      for (String arg:args){buff.append(arg).append(" ");}
      return buff;
   }

    public static void print(List<Lexeme> lexemes)
   {
      for(Lexeme lexeme : lexemes)
      {
         System.out.printf("[%s, %s]%n",lexeme.getTerminal().getTokenType(),lexeme.getValue());
      }
   }

}
