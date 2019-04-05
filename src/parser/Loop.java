package parser;

import javafx.util.Pair;

import static parser.Parser.*;

class Loop {
    static void L() {
        tabs(++level); writer.println("L");

        tabs(level + 1); writer.println(look);
        match();

        Pair<Integer, Integer> pair = Condition.C1();
        int c1t = pair.getKey();
        int c1f = pair.getValue();
        backPatch(c1t, Integer.toString(n));

        B();

        emit("goto", Integer.toString(c1t));
        backPatch(c1f, Integer.toString(n));

        level--;
    }
}
