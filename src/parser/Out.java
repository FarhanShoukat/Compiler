package parser;

import classes.Token;
import classes.TokenEnum;

import static parser.Parser.*;

class Out {
    static void O() {
        tabs(++level); writer.println("O");

        tabs(level + 1); writer.println(look);
        match();

        R();

        Token token = CharacterChecks.checkCharacter(';');
        tabs(level + 1); writer.println(token);

        level--;
    }

    private static void R() {
        tabs(++level); writer.println("R");

        if(look != null && look.token_type.equals(TokenEnum.STR)) {
            String strLex = look.lexeme.toString();
            tabs(level + 1); writer.println(look);
            match();

            emit("out", strLex);
        }
        else {
            Object en = Expression.E();

            emit("out", en.toString());
        }

        level--;
    }
}