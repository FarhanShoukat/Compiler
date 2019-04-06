package parser;

import classes.Token;
import classes.TokenEnum;
import javafx.util.Pair;

import java.util.HashMap;

import static parser.Parser.*;

class Condition {
    private static HashMap<TokenEnum, String> map = new HashMap<TokenEnum, String>() {{
        put(TokenEnum.LT, "<");
        put(TokenEnum.LE, "<=");
        put(TokenEnum.GT, ">");
        put(TokenEnum.GE, ">=");
        put(TokenEnum.EQ, "==");
        put(TokenEnum.NE, "!=");
    }};

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
        emit("if", e1n.toString(), map.get(roLex), e2n.toString(), "goto");
        int c1f = n;
        emit("goto");

        level--;

        return new Pair<>(c1t, c1f);
    }
}
