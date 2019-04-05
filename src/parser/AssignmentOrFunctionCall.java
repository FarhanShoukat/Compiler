package parser;

import classes.Token;
import classes.TokenEnum;

import static parser.Parser.*;

class AssignmentOrFunctionCall {
    static void A() {
        tabs(++level); writer.println("A");

        tabs(level + 1); writer.println(look);
        String r111p = look.lexeme.toString();
        match();

        R111(r111p);

        Token token = CharacterChecks.checkCharacter(';');
        tabs(level + 1); writer.println(token);

        level--;
    }

    private static void R111(String p) {
        tabs(++level); writer.println("R'''");

        if(look != null && look.token_type.equals(TokenEnum.EQ)) {
            tabs(level + 1); writer.println(look);
            match();

            A1(p);
        }
        else {
            Token token = CharacterChecks.checkCharacter('(');
            tabs(level + 1); writer.println(token);

            PRS();

            token = CharacterChecks.checkCharacter(')');
            tabs(level + 1); writer.println(token);
        }

        level--;
    }

    private static void A1(String p) {
        tabs(++level); writer.println("A'");

        Token next;
        if(index < tokens.size()) next = tokens.get(index); else next = null;
        if(look != null && look.token_type.equals(TokenEnum.ID) && next != null && next.token_type.equals('(')) {
            tabs(level + 1); writer.println(look);
            match();

            tabs(level + 1); writer.println(next);
            match();

            PRS();

            next = CharacterChecks.checkCharacter(')');
            tabs(level + 1); writer.println(next);
        }
        else {
            Object en = Expression.E();
            emit(p, "=", en.toString());
        }

        level--;
    }

    private static void PRS() {
        tabs(++level); writer.println("PRS");

        if(look == null || look.token_type.equals(')')) {
            tabs(level + 1); writer.println("null");
        }
        else {
            PR();
        }

        level--;
    }

    private static void PR() {
        tabs(++level); writer.println("PR");

        Expression.E();
        PR1();

        level--;
    }

    private static void PR1() {
        tabs(++level); writer.println("PR'");

        if(look != null && look.token_type.equals(',')) {
            tabs(level + 1); writer.println(look);
            match();

            PR();
        }
        else {
            tabs(level + 1); writer.println("null");
        }

        level--;
    }
}
