package parser;

import classes.Identifier;
import classes.Token;
import classes.TokenEnum;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import static Lex.Lex.getCurrentLineNo;

public class Parser {
    static ArrayList<Token> tokens;
    static HashMap<String, Identifier> identifiers;

    static Token look;
    static int index = 0;
    private static int totalTokens = 0;

    static String code;

    static int level = -1;
    static PrintWriter writer = null;

    public static void Parse(ArrayList<Token> tokens, HashMap<String, Identifier> identifiers, String code) {
        Parser.tokens = tokens;
        Parser.identifiers = identifiers;
        Parser.code = code;

        if(!Parser.tokens.isEmpty()) {
            totalTokens = Parser.tokens.size();
            match();

            try {
                writer = new PrintWriter("parsetree.txt");
                S();
                writer.close();

                writer = new PrintWriter("parser-symboltable.txt");
                writer.println("Identifier\tType");
                for (String id : identifiers.keySet()) {
                    writer.println(String.format("%s\t%s", id, identifiers.get(id).type));
                }
                writer.close();

                System.out.println("Parse tree created successfully!");
            }
            catch(IOException ignored) {}
        }
    }

    private static void S() {
        tabs(++level); writer.println("S");

        if(look == null) {
            tabs(level + 1);
            writer.println("null");
        }
        else {
            Object token_type = look.token_type;

            if(token_type instanceof Identifier.Type) {
                DeclarationOutsideFunction.D();

            }
            else if(token_type.equals(TokenEnum.WHILE)) {
                Loop.L();
            }
            else if(token_type.equals(TokenEnum.IF)) {
                Conditional.C();
            }
            else if(token_type.equals(TokenEnum.IN)) {
                In.I();
            }
            else if(token_type.equals(TokenEnum.OUT)) {
                Out.O();
            }
            else if(token_type.equals(TokenEnum.ID)) {
                AssignmentOrFunctionCall.A();
            }
            else {
                System.out.println(String.format("line %d: unexpected token", getCurrentLineNo(code, look.startingIndex)));
                System.exit(0);
            }

            S();
        }
        level--;
    }

    private static void S1() {
        tabs(++level); writer.println("S'");

        if(look == null) {
            tabs(level + 1); writer.println("null");
        }
        else {
            Object token_type = look.token_type;

            if(token_type instanceof Identifier.Type) {
                DeclarationInsideFunction.D11();

            }
            else if(token_type.equals(TokenEnum.WHILE)) {
                Loop.L();
            }
            else if(token_type.equals(TokenEnum.IF)) {
                Conditional.C();
            }
            else if(token_type.equals(TokenEnum.RET)) {
                Return.R();
            }
            else if(token_type.equals(TokenEnum.IN)) {
                In.I();
            }
            else if(token_type.equals(TokenEnum.OUT)) {
                Out.O();
            }
            else if(token_type.equals(TokenEnum.ID)) {
                AssignmentOrFunctionCall.A();
            }
            else {
                tabs(level + 1); writer.println("null");
                level--;
                return;
            }

            S1();
        }

        level--;
    }

    static void T() {
        tabs(++level); writer.println("T");
        tabs(level + 1); writer.println(look);

        match();

        --level;
    }

    static void B() {
        tabs(++level); writer.println("B");

        Token token = CharacterChecks.checkCharacter('{');
        tabs(level + 1); writer.println(token);

        Parser.S1();

        token = CharacterChecks.checkCharacter('}');
        tabs(level + 1); writer.println(token);

        level--;
    }

    static void match() { if(index < totalTokens) look = tokens.get(index++); else look = null; }

    static void tabs(int amount) { for(int i = 0; i < amount; i++) writer.print('\t'); }
}