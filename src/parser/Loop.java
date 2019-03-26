package parser;

import static parser.Parser.*;

class Loop {
    static void L() {
        tabs(++level); writer.println("L");

        tabs(level + 1); writer.println(look);
        match();

        Condition.C1();

        B();

        level--;
    }
}
