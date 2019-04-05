package parser;

import classes.Token;
import classes.TokenEnum;
import javafx.util.Pair;

import static parser.Parser.*;

class Condition {
    static Pair<Integer, Integer> C1() {
        tabs(++level); writer.println("C'");

        Token token = CharacterChecks.checkCharacter('(');
        tabs(level + 1); writer.println(token);

        Object e1n = Expression.E();

        if(look == null || !look.token_type.equals(TokenEnum.RO)) Errors.relationalOperatorMissing();

        Object roLex = look.lexeme;
        tabs(level + 1); writer.println(look);
        match();

        Object e2n = Expression.E();

        token = CharacterChecks.checkCharacter(')');
        tabs(level + 1); writer.println(token);

        int c1t = n;
        emit("if", e1n.toString(), roLex.toString(), e2n.toString(), "goto");
        int c1f = n;
        emit("goto");

        level--;

        return new Pair<>(c1t, c1f);
    }
}
