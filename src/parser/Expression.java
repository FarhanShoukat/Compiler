package parser;

import classes.Identifier;
import classes.Token;
import classes.TokenEnum;

import static parser.Parser.*;

class Expression {
    static void E() {
        tabs(++level); writer.println("E");

        T();

        E1();

        level--;
    }

    private static void E1() {
        tabs(++level); writer.println("E'");

        if(look != null) {
            if(look.token_type.equals('+') || look.token_type.equals('-')) {
                tabs(level + 1); writer.println(look);
                match();

                T();

                E1();
            }
            else {
                tabs(level + 1); writer.println("null");
            }
        }

        level--;
    }

    private static void T() {
        tabs(++level); writer.println("T");

        F();

        T1();

        level--;
    }

    private static void T1() {
        tabs(++level); writer.println("T'");

        if(look != null) {
            if(look.token_type.equals('*') || look.token_type.equals('/')) {
                tabs(level + 1); writer.println(look);
                match();

                F();

                T1();
            }
            else {
                tabs(level + 1); writer.println("null");
            }
        }

        level--;
    }

    private static void F() {
        tabs(++level); writer.println("F");

        if(look == null) Errors.expressionMissing();

        if(look.token_type.equals(TokenEnum.ID)) {
//            Identifier.Type type = identifiers.get(look.lexeme.toString()).type;
//            if(type == null)
//                Errors.identifierNotDefined();
//            else if(!(type.equals(Identifier.Type.CHAR) || type.equals(Identifier.Type.INT)))
//                Errors.identifierNotDefined();

            tabs(level + 1); writer.println(look);

            match();
        }
        else if(look.token_type.equals(TokenEnum.NUM) || look.token_type.equals(TokenEnum.CL)) {
            tabs(level + 1); writer.println(look);

            match();
        }
        else if(look.token_type.equals('(')) {
            tabs(level + 1); writer.println(look);
            match();

            E();

            Token token = CharacterChecks.checkCharacter(')');
            tabs(level + 1); writer.println(token);
        }
        else if(look.token_type.equals('[')) {
            tabs(level + 1); writer.println(look);
            match();

            E();

            Token token = CharacterChecks.checkCharacter(']');
            tabs(level + 1); writer.println(token);
        }
        else {
            Errors.expressionMissing();
        }

        level--;
    }
}