package parser;

import static Lex.Lex.getCurrentLineNo;

import static parser.Parser.*;

public class Errors {
    static void identifierMissing() {
        System.out.println(String.format("line %d: variable expected", getCurrentLineNo(code, look != null ? look.startingIndex : code.length() - 1)));
        System.exit(0);
    }

    static void identifierAlreadyDefined() {
        System.out.println(String.format("line %d: variable '%s' already defined", getCurrentLineNo(code, look != null ? look.startingIndex : code.length() - 1), look.lexeme));
        System.exit(0);
    }

    static void identifierNotDefined() {
        System.out.println(String.format("line %d: cannot resolve symbol '%s'", getCurrentLineNo(code, look != null ? look.startingIndex : code.length() - 1), look.lexeme));
        System.exit(0);
    }

    static void expressionMissing() {
        System.out.println(String.format("line %d: expression expected", getCurrentLineNo(code, look != null ? look.startingIndex : code.length() - 1)));
        System.exit(0);
    }

    public static void characterLiteralMissing() {
        System.out.println(String.format("line %d: character literal expected", getCurrentLineNo(code, look != null ? look.startingIndex : code.length() - 1)));
        System.exit(0);
    }

    static void relationalOperatorMissing() {
        if(look == null)
            characterMissing(')');
        else
            System.out.println(String.format("line %d: relational operator expected", getCurrentLineNo(code, look.startingIndex)));
        System.exit(0);
    }

    static void characterMissing(char c) {
        System.out.println(String.format("line %d: '%c' expected", getCurrentLineNo(code, look != null ? look.startingIndex : code.length() - 1), c));
        System.exit(0);
    }
}
