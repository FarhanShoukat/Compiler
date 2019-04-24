package parser;

import classes.Quadruple;
import classes.Token;

import static parser.Parser.*;

class Return {
    static void R() {
        tabs(++level); writer.println("R");

        tabs(level + 1); writer.println(look);
        match();

        String en = Expression.E();

        Token token = CharacterChecks.checkCharacter(';');
        tabs(level + 1); writer.println(token);

        emit("return", en);
        quadruples.add(new Quadruple(Quadruple.OPCODE_RET, getPair(en)));

        level--;
    }
}