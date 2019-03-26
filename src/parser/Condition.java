package parser;

import classes.Token;
import classes.TokenEnum;

import static parser.Parser.*;

class Condition {
    static void C1() {
        tabs(++level); writer.println("C'");

        Token token = CharacterChecks.checkCharacter('(');
        tabs(level + 1); writer.println(token);

        Expression.E();

        if(look == null || !look.token_type.equals(TokenEnum.RO)) Errors.relationalOperatorMissing();

        tabs(level + 1); writer.println(look);
        match();

        Expression.E();

        token = CharacterChecks.checkCharacter(')');
        tabs(level + 1); writer.println(token);

        level--;
    }
}
