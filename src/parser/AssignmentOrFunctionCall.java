package parser;

import classes.Function;
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

            String a1n = A1();
            emit(p, "=", a1n);
        }
        else {
            Token token = CharacterChecks.checkCharacter('(');
            tabs(level + 1); writer.println(token);

            int prsc = PRS();

            token = CharacterChecks.checkCharacter(')');
            tabs(level + 1); writer.println(token);

            String temp = newTemp(((Function)identifiers.get(p)).returnType);
            emit("call", p, ",", Integer.toString(prsc), ",", temp);
        }

        level--;
    }

    private static String A1() {
        tabs(++level); writer.println("A'");

        String n;

        Token next;
        if(index < tokens.size()) next = tokens.get(index); else next = null;
        if(look != null && look.token_type.equals(TokenEnum.ID) && next != null && next.token_type.equals('(')) {
            tabs(level + 1); writer.println(look);
            Token id = look;
            match();

            tabs(level + 1); writer.println(next);
            match();

            int prsc = PRS();

            next = CharacterChecks.checkCharacter(')');
            tabs(level + 1); writer.println(next);

            n = newTemp(((Function)identifiers.get(id.lexeme.toString())).returnType);
            emit("call", id.lexeme.toString(), ",", Integer.toString(prsc), ",", n);
        }
        else {
            String en = Expression.E();
            n = en;
        }

        level--;

        return n;
    }

    private static int PRS() {
        tabs(++level); writer.println("PRS");

        int c;

        if(look == null || look.token_type.equals(')')) {
            tabs(level + 1); writer.println("null");

            c = 0;
        }
        else {
            c = PR();
        }

        level--;

        return c;
    }

    private static int PR() {
        tabs(++level); writer.println("PR");

        String en = Expression.E();
        emit("parm", en);

        int c = PR1() + 1;

        level--;

        return c;
    }

    private static int PR1() {
        tabs(++level); writer.println("PR'");

        int c;

        if(look != null && look.token_type.equals(',')) {
            tabs(level + 1); writer.println(look);
            match();

            c = PR();
        }
        else {
            tabs(level + 1); writer.println("null");

            c = 0;
        }

        level--;

        return c;
    }
}
