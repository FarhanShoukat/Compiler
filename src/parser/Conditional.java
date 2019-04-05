package parser;

import classes.TokenEnum;
import javafx.util.Pair;

import static parser.Parser.*;

class Conditional {
    static void C() {
        tabs(++level); writer.println("C");

        tabs(level + 1); writer.println(look);
        match();

        Pair<Integer, Integer> pair = Condition.C1();
        int c1t = pair.getKey();
        int c1f = pair.getValue();
        backPatch(c1t, Integer.toString(n));

        B();

        O1(c1f);

        level--;
    }

    private static void O1(int f) {
        tabs(++level); writer.println("O'");

        if(look != null && look.token_type.equals(TokenEnum.ELSE)) {
            tabs(level + 1); writer.println(look);
            match();

            int next = n;
            emit("goto");
            backPatch(f, Integer.toString(n));

            B();

            backPatch(next, Integer.toString(n));
        }
        else {
            tabs(level + 1); writer.println("null");

            backPatch(f, Integer.toString(n));
        }

        level--;
    }
}
