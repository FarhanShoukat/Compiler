package parser;

import classes.Identifier;
import classes.Token;

import static Lex.Lex.getCurrentLineNo;
import static parser.Parser.*;

class DeclarationOutsideFunction {
    static void D() {
        tabs(++level); writer.println("D");

        Identifier.Type type = (Identifier.Type) look.token_type;

        int size = D1();

        R11(type, size);

        level--;
    }

    static int D1() {
        tabs(++level); writer.println("D'");

        if(look == null || !(look.token_type instanceof Identifier.Type)) {
            System.out.println(String.format("line %d: type expected", getCurrentLineNo(code, look != null ? look.startingIndex : code.length() - 1)));
            System.exit(0);
        }
        Identifier.Type type = (Identifier.Type) look.token_type;

        int size = T();

        //identifier ID
        Token token = CharacterChecks.checkIdentifier(type, size);
        tabs(level + 1); writer.println(token);

        level--;

        return size;
    }

    private static int T() {
        tabs(++level); writer.println("T");

        tabs(level + 1); writer.println(look);

        int size = -1;
        if(look.token_type.equals(Identifier.Type.INT))
            size = 4;
        else if(look.token_type.equals(Identifier.Type.CHAR))
            size = 1;

        match();

        --level;

        return size;
    }

    private static void R11(Identifier.Type type, int size) {
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
            O11(type, size);
        }

        level--;
    }

    static void O11(Identifier.Type type, int size) {
        tabs(++level); writer.println("O''");

        if(look != null && look.token_type.equals(',')) {
            tabs(level + 1); writer.println(look);

            match();

            Token token = CharacterChecks.checkIdentifier(type, size);
            tabs(level + 1); writer.println(token);

            O11(type, size);
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
