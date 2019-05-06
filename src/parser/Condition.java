package parser;

import classes.Quadruple;
import classes.Token;
import classes.TokenEnum;

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

    private static HashMap<TokenEnum, Integer> map1 = new HashMap<TokenEnum, Integer>() {{
        put(TokenEnum.LT, Quadruple.OPCODE_LT);
        put(TokenEnum.LE, Quadruple.OPCODE_LE);
        put(TokenEnum.GT, Quadruple.OPCODE_GT);
        put(TokenEnum.GE, Quadruple.OPCODE_GE);
        put(TokenEnum.EQ, Quadruple.OPCODE_EQ);
        put(TokenEnum.NE, Quadruple.OPCODE_NE);
    }};

    static Pair C1() {
        tabs(++level); writer.println("C'");

        Token token = CharacterChecks.checkCharacter('(');
        tabs(level + 1); writer.println(token);

        String e1n = Expression.E();

        if(look == null || !look.token_type.equals(TokenEnum.RO)) Errors.relationalOperatorMissing();

        Object roLex = look.lexeme;
        tabs(level + 1); writer.println(look);
        match();

        String e2n = Expression.E();

        token = CharacterChecks.checkCharacter(')');
        tabs(level + 1); writer.println(token);

        int c1t = n;
        emit("if", e1n, map.get(roLex), e2n, "goto");
        quadruples.add(new Quadruple(map1.get(roLex), getPair(e1n), getPair(e2n)));

        int c1f = n;
        emit("goto");
        quadruples.add(new Quadruple(Quadruple.OPCODE_GOTO));

        level--;

        return new Pair(c1t, c1f);
    }
}
