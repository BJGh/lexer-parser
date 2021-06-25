import lexer.Lexeme;
import lexer.Lexer;
import parser.Parser;

import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args)
    {
        String s = "a = 1; b = 2; v = a + b; if(a>c) {b=b+1;}  ";
        Lexer.print(Lexer.getTokenList(s));
        List<Lexeme> delDuplicate = Lexer.getTokenList(s);


        Parser parser = new Parser();

        parser.setLexemes(delDuplicate.stream().distinct().collect(Collectors.toList()));
        parser.parse();
        List<Lexeme> rpn  = parser.toRPN(parser.getLexemes());
        for(Lexeme token_rpn:rpn.stream().distinct().collect(Collectors.toList()))
        {
            System.out.printf("%-5s",token_rpn.getValue()+" ");
        }

    }
}
