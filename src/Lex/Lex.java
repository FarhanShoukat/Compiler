package Lex;

import classes.Identifier;
import classes.Token;
import classes.TokenEnum;
import parser.Parser;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Lex {
    private static final HashSet<Character> NEW_LINE_CHARS = new HashSet<>(Arrays.asList('\n', '\r', '\u0085', '\u2028', '\u2029'));
    private static final HashSet<Character> SKIP_CHARS = new HashSet<>(Arrays.asList('\n', '\r', '\u0085', '\u2028', '\u2029', ' ', '\t'));

    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    private static final HashMap<String, Object> KEYWORDS = new HashMap<String, Object>() {{
        put("int", Identifier.Type.INT);
        put("char", Identifier.Type.CHAR);
        put("if", TokenEnum.IF);
        put("else", TokenEnum.ELSE);
        put("while", TokenEnum.WHILE);
        put("ret", TokenEnum.RET);
        put("in", TokenEnum.IN);
        put("out", TokenEnum.OUT);
    }};

    private static final ArrayList<Token> tokens = new ArrayList<>();
    private static final HashMap<String, Identifier> identifiers = new HashMap<>();

    private static String code;
    private static int codeLength;
    private static int currentIndex = 0;

    private static int state = 1;


    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    // returns the line number of code file which is currently being read
    public static long getCurrentLineNo(String code, int currentIndex) {
        String codeTillCurrentIndex = code.substring(0, currentIndex);
        return Math.max(codeTillCurrentIndex.chars().filter(ch -> ch == '\r').count(), codeTillCurrentIndex.chars().filter(ch -> ch == '\n').count()) + 1;
    }

    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    // identifies IDs (variables) and language keywords
    private static void checkIdentifierOrKeyword() {
        for(int i = currentIndex + 1; i < codeLength; i++) {
            // check if we got a character other than letter or digit, or if we
            // have reached the end of code file.
            if(!Character.isLetterOrDigit(code.charAt(i))) {
                String substring = code.substring(currentIndex, i);

                if(KEYWORDS.containsKey(substring)) {
                    tokens.add(new Token(KEYWORDS.get(substring), currentIndex));
                }
                else {
                    identifiers.put(substring, new Identifier());
                    tokens.add(new Token(TokenEnum.ID, substring, currentIndex));
                }

                currentIndex = i;

                return;
            }
        }

        System.out.println(String.format("line %1$d: ';' expected.", getCurrentLineNo(code, currentIndex)));
        System.exit(0);
    }

    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    // identifies an integer
    private static void checkInteger() {
        for(int i = currentIndex + 1; i < codeLength; i++) {
            // check if we got a character other than digit, or if we
            // have reached the end of code file.
            if(!Character.isDigit(code.charAt(i))) {
                tokens.add(new Token(TokenEnum.NUM, Integer.parseInt(code.substring(currentIndex, i)), currentIndex));
                currentIndex = i;

                return;
            }
        }

        System.out.println(String.format("line %d: ';' expected", getCurrentLineNo(code, currentIndex)));
        System.exit(0);
    }

    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////


    // identifies a character literal
    private static void checkCharacter() {
        int index = currentIndex + 2;

        // check for closing single quote
        if(index < codeLength && code.charAt(index) == '\'') {
            tokens.add(new Token(TokenEnum.CL, code.substring(currentIndex, index + 1), currentIndex));
            currentIndex += 3;

        }
        else {
            System.out.println(String.format("line %1$d: Unclosed Character Literal.", getCurrentLineNo(code, currentIndex)));
            System.exit(0);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    // identifies a relational operator
    private static void checkRelationalOperator() {
        switch(code.charAt(currentIndex)) {
            case '<': {
                state = 31;
                break;
            }
            case '>': {
                state = 32;
                break;
            }
            case '=': {
                state = 33;
                break;
            }
            case '!': {
                state = 34;
                break;
            }
        }

        currentIndex++;

        switch (state) {
            case 31: {
                if(currentIndex < codeLength && code.charAt(currentIndex) == '=') {
                    tokens.add(new Token(TokenEnum.RO, TokenEnum.LE, currentIndex - 1));
                    currentIndex++;
                }
                else {
                    tokens.add(new Token(TokenEnum.RO, TokenEnum.LT, currentIndex - 1));
                }
                return;
            }
            case 32: {
                if(currentIndex < codeLength && code.charAt(currentIndex) == '=') {
                    tokens.add(new Token(TokenEnum.RO, TokenEnum.GE, currentIndex - 1));
                    currentIndex++;
                }
                else {
                    tokens.add(new Token(TokenEnum.RO, TokenEnum.GT, currentIndex - 1));
                }
                return;
            }
            case 33: {
                if(currentIndex < codeLength && code.charAt(currentIndex) == '=') {
                    tokens.add(new Token(TokenEnum.RO, TokenEnum.EQ, currentIndex - 1));
                    currentIndex++;
                    return;
                }
                else {
                    System.out.println(String.format("line %1$d: Unidentified Operator '='", getCurrentLineNo(code, currentIndex)));
                    System.exit(0);
                }
            }
            case 34: {
                if(currentIndex < codeLength && code.charAt(currentIndex) == '=') {
                    tokens.add(new Token(TokenEnum.RO, TokenEnum.NE, currentIndex - 1));
                    currentIndex++;
                }
                else {
                    // logical NOT operator
                    tokens.add(new Token('!', currentIndex - 1));
                }
            }
        }

    }

    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    // check for comments in code
    private static void checkComment() {
        currentIndex++;

        for(; currentIndex < codeLength; currentIndex++) {
            switch (state) {
                case 50: {
                    if (code.charAt(currentIndex) == '#')
                        state = 51;    //it seems to be a multiple line comment
                    else
                        state = 53; //it seems to be a single line comment

                    break;
                }
                case 51: {
                    if (code.charAt(currentIndex) == '#')
                        state = 52;

                    break;
                }
                case 52: {
                    if (code.charAt(currentIndex) == '#') {
                        currentIndex++;
                        return;
                    }
                    else
                        state = 51;

                    break;
                }
                case 53: {
                    if (NEW_LINE_CHARS.contains(code.charAt(currentIndex)))
                        return;

                    break;
                }
            }
        }

    }

    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////


    // identifies the STR token and its lexeme
    private static void checkString() {
        // find the index of the enclosing double quotes
        int index = code.indexOf('"', currentIndex + 1);

        // if enclosing quotes are not found
        if (index == -1) {
            System.out.println(String.format("line %1$d: Unclosed string literal.", getCurrentLineNo(code, currentIndex)));
            System.exit(0);
        }
        else {
            tokens.add(new Token(TokenEnum.STR, code.substring(currentIndex, index + 1), currentIndex));
            currentIndex = index + 1;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    // main function
    public static void Lex(String[] args) {
        if (args == null || args.length == 0) {
            System.out.println("File name not given.");
            return;
        }

        try {
            code = new String(Files.readAllBytes(Paths.get(args[0])));
        }
        catch (IOException exception) {
            System.out.println(exception.getMessage());
            return;
        }

        codeLength = code.length();

        // loop through the whole file
        while(currentIndex < codeLength) {
            state = 1;

            //skipping all waste characters
            while (currentIndex < codeLength && SKIP_CHARS.contains(code.charAt(currentIndex))) currentIndex++;

            char c = code.charAt(currentIndex);
            if(c == '#') {
                state = 50;
                checkComment();
            }
            else if(c == '+' || c == '-' || c == '*' || c == '/' || c == '{' || c == '}' || c == '(' || c == ')' || c == ',' || c == ';') {
                tokens.add(new Token(c, currentIndex));
                currentIndex++;
            }
            else if(c == ':') {
                int i = currentIndex + 1;
                if(i < codeLength && code.charAt(i) == '=') {
                    tokens.add(new Token(TokenEnum.EQ, currentIndex));
                    currentIndex += 2;
                }
                else {
                    System.out.println(String.format("line %1$d: Unidentified Operator ':'", getCurrentLineNo(code, currentIndex)));
                    return;
                }
            }
            else if(Character.isLetter(c)) {
                state = 10;
                checkIdentifierOrKeyword();
            }
            else if(Character.isDigit(c)) {
                state = 20;
                checkInteger();
            }
            else if(c == '<' || c == '>' || c == '=' || c == '!') {
                state = 30;
                checkRelationalOperator();
            }
            else if(c == '\'') {
                state = 40;
                checkCharacter();
            }
            else if(c == '"') {
                state = 60;
                checkString();
            }
            else {
                System.out.println(String.format("line %d: Unidentified Operator or Character '%s'", getCurrentLineNo(code, currentIndex), c));
                return;
            }
        }

        // write the results if the code file is tokenized successfully
        try {
            PrintWriter writer = new PrintWriter("words.txt");
            for (Token token : tokens) {
                writer.println(token);
            }
            writer.close();

            writer = new PrintWriter("symboltable.txt");
            for (String id : identifiers.keySet()) {
                writer.println(id);
            }
            writer.close();

            //System.out.println("Tokens created successfully!");

            Parser.Parse(tokens, identifiers, code);
        }
        catch (IOException ignored) {}
    }

    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
}