package parser;

import classes.TokenEnum;

import static parser.Parser.*;

class Conditional {
    static void C() {
        tabs(++level); writer.println("C");

        tabs(level + 1); writer.println(look);
        match();

        Condition.C1();

        B();

        O1();

        level--;
    }

    private static void O1() {
        tabs(++level); writer.println("O'");

        if(look != null && look.token_type.equals(TokenEnum.ELSE)) {
            tabs(level + 1); writer.println(look);
            match();

            B();
        }
        else {
            tabs(level + 1); writer.println("null");
        }

        level--;
    }
}
