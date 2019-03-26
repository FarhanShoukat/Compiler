package parser;

import classes.Token;

import static parser.Parser.*;

class Return {
    static void R() {
        tabs(++level); writer.println("R");

        tabs(level + 1); writer.println(look);
        match();

        Expression.E();

        Token token = CharacterChecks.checkCharacter(';');
        tabs(level + 1); writer.println(token);

        level--;
    }
}