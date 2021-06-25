package parser;

import lexer.Lexeme;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {
    private Stack<Lexeme> operators = new Stack<>();
    private List<Lexeme> lexemes;
    public List<Lexeme> rpn;

    private Integer point = 0;



    public List<Lexeme> getLexemes() {
        return lexemes;
    }

    public List<Lexeme> getRpn(){return rpn;}

    public void setLexemes(List<Lexeme> lexemes) {
        this.lexemes = lexemes;
        delWhitespace(lexemes);
    }
    private void delWhitespace(List<Lexeme> lexemes)
    {

        for(int i=0; i<lexemes.size();i++)
        {
            if(lexemes.get(i).getTerminal().getTokenType().equals("WHITESPACE"))
            {
                lexemes.remove(i);
            }
        }
    }

    public void parse()
    {

        lang();
        System.out.println("[Parser] no syntax error were found");
       //
        System.out.println("[Parser] RPN");


    }
    public void lang() {


        delWhitespace(lexemes);

        expr();
        while(List.of("VAR", "IF_KEYWORD", "WHILE_KEYWORD").contains(currentToken().getTerminal().getTokenType())) {
            expr();
        }

    }

    private void expr() throws RuntimeException {
        if(currentToken().getTerminal().getTokenType().equals("VAR")) {
            assign_expr();
        }
        else if(currentToken().getTerminal().getTokenType().equals("IF_KEYWORD")) {
            if_expr();
        }
        else if(currentToken().getTerminal().getTokenType().equals("WHILE_KEYWORD")) {
            while_expr();
        }
    }

    private void assign_expr() {

        match(List.of("VAR"));
        match(List.of("ASSIGN"));

        math_expr();

      //  match(List.of("SC"));

    }

    private void math_expr() {

        if(List.of("VAR", "NUMBER").contains(currentToken().getTerminal().getTokenType())) {
            value();
        }
        else if(List.of("LEFT_BRACE").contains(currentToken().getTerminal().getTokenType())) {
            math_expr_brackets();
        }
        while(List.of("MULDIV","ADDSUB").contains(currentToken().getTerminal().getTokenType())) {
            match(List.of("MULDIV"));
            match(List.of("ADDSUB"));
            if(List.of("VAR", "NUMBER", "LEFT_BRACE").contains(currentToken().getTerminal().getTokenType())) {
                math_expr();
            }
            else {
                throw new RuntimeException("Invalid token");
            }
        }

    }

    private void math_expr_brackets() {

        match(List.of("LEFT_BRACE"));
        math_expr();
        match(List.of("RIGHT_BRACE"));
    }

    private void value() {
        match(List.of("NUMBER", "VAR"));
    }

    private void if_expr() {
        if_head();
        body();
        if(lexemes.size()<point && List.of("ELSE_KEYWORD").contains(currentToken().getTerminal().getTokenType())) {
            else_head();
            body();
        }
    }

    private void if_head() {
        match(List.of("IF_KEYWORD"));
        condition();
    }

    private void condition() {
        match(List.of("LEFT_BRACE"));
        logical_expr();
        match(List.of("RIGHT_BRACE"));
    }

    private void logical_expr() {
        value();
        while(List.of("LOGICAL_OP").contains(currentToken().getTerminal().getTokenType())) {
            match(List.of("LOGICAL_OP"));
            value();
        }
    }

    private void body() {
        match(List.of("LEFT_PAREN"));
        while(List.of("VAR", "IF_KEYWORD", "WHILE_KEYWORD").contains(currentToken().getTerminal().getTokenType())) {
            expr();
        }
        match(List.of("RIGHT_PAREN"));
    }

    private void else_head() {
        match(List.of("ELSE_KEYWORD"));
    }

    private void while_expr() {
        while_head();
        body();
    }

    private void while_head() {
        match(List.of("WHILE_KEYWORD"));
        condition();
    }

    private Lexeme currentToken() {
      if(point >= lexemes.size()) {
          point = lexemes.size() - 1;
      }
        return lexemes.get(point);

    }

    private void match(List<String> terminal) {
        if(terminal.contains(currentToken().getTerminal().getTokenType())) {
            point++;
        }
        else {
            throw new RuntimeException("Invalid token");
        }
    }

    public List<Lexeme> toRPN(List<Lexeme> tokens)
    {
        List<Lexeme> out = new ArrayList<>();
        for(int i = 0; i<tokens.size();i++)
        {
            switch (tokens.get(i).getTerminal().getTokenType())
            {

                case "VAR": i = _assign_expr(i,tokens,out);
                break;
                case "IF_KEYWORD": i = _if_expr(i,tokens,out);
                break;
            }
        }
        return out;
    }



    private int _assign_expr(int index, List<Lexeme> tokens, List<Lexeme> out)
    {
        out.add(tokens.get(index));
        for(int i = index +1; i<tokens.size();i++)
        {
            switch (tokens.get(i).getTerminal().getTokenType())
            {
                case "VAR":
                case "NUMBER":
                    out.add(tokens.get(i));
                    break;
                case "MULDIV":
                case "ADDSUB":
                    if (!operators.empty()) {
                        while (tokens.get(i).getTerminal().getPriority() <= operators.peek().getTerminal().getPriority())
                            out.add(operators.pop());
                        operators.push(tokens.get(i));
                    } else
                        operators.push(tokens.get(i));
                    break;
                case "ASSIGN":
                    operators.push(tokens.get(i));
                    break;
                case "SC":
                    while (!operators.empty())
                        out.add(operators.pop());
                    out.add(tokens.get(i));
                    break;





            }
        }
        return index;
    }
    private int _if_expr(int index, List<Lexeme> tokens,List<Lexeme>out)
    {
           int start_indx=0;
        for(int condition_i = index +1; condition_i < tokens.size();condition_i++)
        {
            switch(tokens.get(condition_i).getTerminal().getTokenType())
            {
                case "LEFT_PAREN":
                 operators.push(tokens.get(condition_i));
                 break;
                case "VAR":
                case "NUM":
                    out.add(tokens.get(condition_i));
                    break;
                case "LOGICAL_OP":
                    operators.push(tokens.get(condition_i));
                    start_indx = out.size() -1;

                    break;

                case "RIGHT_PAREN":
                    while (!operators.peek().getTerminal().getTokenType().equals("LEFT_PAREN"))
                        out.add(operators.pop());
                    operators.pop();
                    break;
                case "LEFT_BRACE":
                    for(int body_i = condition_i +1;body_i<tokens.size();body_i++)
                {
                    switch (tokens.get(body_i).getTerminal().getTokenType())
                    {
                        case "IF_KEYWORD":
                            body_i = _if_expr(body_i,tokens,out);
                            break;
                        case "RIGHT_BRACE":
                       break;
                       // Lexeme p1 = new Lexeme("p","p");
                        default:
                        body_i = _assign_expr(body_i,tokens,out);
                    }
                }
            }
        }
    return index;
    }

    public void PrintRPN(){
        for(Lexeme token_rpn:rpn)
        {
         System.out.printf("%-5s",token_rpn.getValue()+" ");
        }
    }
}
