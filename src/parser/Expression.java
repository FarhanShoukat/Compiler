package parser;

import classes.Token;
import classes.TokenEnum;

import static parser.Parser.*;

class Expression {
    static Object E() {
        tabs(++level); writer.println("E");

        Object n = E1(T());

        level--;

        return n;
    }

    private static Object E1(Object p) {
        tabs(++level); writer.println("E'");

        Object n;

        if(look != null) {
            if(look.token_type.equals('+') || look.token_type.equals('-')) {
                tabs(level + 1); writer.println(look);
                Token sign = look;
                match();

                Object tn = T();
                String e1p = newID();

                emit(e1p, "=", p.toString(), sign.token_type.toString(), tn.toString());

                n = E1(e1p);
            }
            else {
                tabs(level + 1); writer.println("null");

                n = p;
            }
        }
        else n = p;

        level--;

        return n;
    }

    private static Object T() {
        tabs(++level); writer.println("T");

        Object n = T1(F());

        level--;

        return n;
    }

    private static Object T1(Object p) {
        tabs(++level); writer.println("T'");

        Object n;

        if(look != null) {
            if(look.token_type.equals('*') || look.token_type.equals('/')) {
                tabs(level + 1); writer.println(look);
                Token sign = look;
                match();

                Object fn = F();
                String t1p = newID();
                emit(t1p, "=", p.toString(), sign.token_type.toString(), fn.toString());

                n = T1(t1p);
            }
            else {
                tabs(level + 1); writer.println("null");

                n = p;
            }
        }
        else n = p;

        level--;

        return n;
    }

    private static Object F() {
        tabs(++level); writer.println("F");

        Object n = null;

        if(look == null) Errors.expressionMissing();

        if(look.token_type.equals(TokenEnum.ID)) {
            tabs(level + 1); writer.println(look);
            n = look.lexeme;
            match();
        }
        else if(look.token_type.equals(TokenEnum.NUM) || look.token_type.equals(TokenEnum.CL)) {
            tabs(level + 1); writer.println(look);
            n = look.lexeme;
            match();
        }
        else if(look.token_type.equals('(')) {
            tabs(level + 1); writer.println(look);
            match();

            n = E();

            Token token = CharacterChecks.checkCharacter(')');
            tabs(level + 1); writer.println(token);
        }
        else if(look.token_type.equals('[')) {
            tabs(level + 1); writer.println(look);
            match();

            n = E();

            Token token = CharacterChecks.checkCharacter(']');
            tabs(level + 1); writer.println(token);
        }
        else {
            Errors.expressionMissing();
        }

        level--;

        return n;
    }
}