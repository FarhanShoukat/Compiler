package parser;

import classes.Identifier;
import classes.Token;

import static Lex.Lex.getCurrentLineNo;
import static parser.Parser.*;

class DeclarationOutsideFunction {
    static void D() {
        tabs(++level); writer.println("D");

        Identifier.Type type = (Identifier.Type) look.token_type;

        D1();

        R11(type);

        level--;
    }

    static void D1() {
        tabs(++level); writer.println("D'");

        if(look == null || !(look.token_type instanceof Identifier.Type)) {
            System.out.println(String.format("line %d: type expected", getCurrentLineNo(code, look != null ? look.startingIndex : code.length() - 1)));
            System.exit(0);
        }
        Identifier.Type type = (Identifier.Type) look.token_type;

        Parser.T();

        //identifier ID
        Token token = CharacterChecks.checkIdentifier(type);
        tabs(level + 1); writer.println(token);

        level--;
    }

    private static void R11(Identifier.Type type) {
        tabs(++level); writer.println("R''");

        if(look != null && look.token_type.equals('(')) {
            tabs(level + 1); writer.println(look);

            match();

            PRD();

            Token token = CharacterChecks.checkCharacter(')');
            tabs(level + 1); writer.println(token);
            B();
        }
        else {
            O11(type);
        }

        level--;
    }

    static void O11(Identifier.Type type) {
        tabs(++level); writer.println("O''");

        if(look != null && look.token_type.equals(',')) {
            tabs(level + 1); writer.println(look);

            match();

            Token token = CharacterChecks.checkIdentifier(type);
            tabs(level + 1); writer.println(token);

            O11(type);
        }
        else {
            Token token = CharacterChecks.checkCharacter(';');
            tabs(level + 1); writer.println(token);
        }

        level--;
    }

    private static void PRD() {
        tabs(++level); writer.println("PRD");

        if(look != null && look.token_type instanceof Identifier.Type) {
            D1();
            R1();
        }
        else {
            tabs(level + 1); writer.println("null");
        }

        level--;
    }

    private static void R1() {
        tabs(++level); writer.println("R'");

        if(look != null && look.token_type.equals(',')) {
            tabs(level + 1); writer.println(look);

            match();

            D1();
            R1();
        }
        else {
            tabs(level + 1); writer.println("null");
        }

        level--;
    }
}
