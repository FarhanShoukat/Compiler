package parser;

import classes.Identifier;
import classes.Token;
import classes.TokenEnum;

import static parser.Parser.*;

class Expression {
    static String E() {
        tabs(++level); writer.println("E");

        String n = E1(T());

        level--;

        return n;
    }

    private static String E1(String p) {
        tabs(++level); writer.println("E'");

        String n;

        if(look != null) {
            if(look.token_type.equals('+') || look.token_type.equals('-')) {
                tabs(level + 1); writer.println(look);
                Token sign = look;
                match();

                String tn = T();
                String e1p = newTemp(getType(p, tn));

                emit(e1p, "=", p, sign.token_type.toString(), tn);

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

    private static String T() {
        tabs(++level); writer.println("T");

        String n = T1(F());

        level--;

        return n;
    }

    private static String T1(String p) {
        tabs(++level); writer.println("T'");

        String n;

        if(look != null) {
            if(look.token_type.equals('*') || look.token_type.equals('/')) {
                tabs(level + 1); writer.println(look);
                Token sign = look;
                match();

                String fn = F();
                String t1p = newTemp(getType(p, fn));
                emit(t1p, "=", p, sign.token_type.toString(), fn);

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

    private static String F() {
        tabs(++level); writer.println("F");

        String n = null;

        if(look == null) Errors.expressionMissing();

        if(look.token_type.equals(TokenEnum.ID)) {
            tabs(level + 1); writer.println(look);
            n = look.lexeme.toString();
            match();
        }
        else if(look.token_type.equals(TokenEnum.NUM) || look.token_type.equals(TokenEnum.CL)) {
            tabs(level + 1); writer.println(look);
            n = look.lexeme.toString();
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

    private static Identifier.Type getType(String str1, String str2) {
        char c1 = str1.charAt(0);
        char c2 = str2.charAt(0);

        if(Character.isDigit(c1) || Character.isDigit(c2))
            return Identifier.Type.INT;
        else if(c1 == '\'' || c2 == '\'')
            return Identifier.Type.CHAR;
        else {
            Identifier id1 = identifiers.getOrDefault(str1, translatorIdentifiers.get(str1));
            Identifier id2 = identifiers.getOrDefault(str2, translatorIdentifiers.get(str2));
            if(id1.type.equals(Identifier.Type.INT) || id2.type.equals(Identifier.Type.INT))
                return Identifier.Type.INT;
            else
                return Identifier.Type.CHAR;
        }
    }
}