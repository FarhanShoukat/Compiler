package parser;

import classes.Quadruple;
import classes.TokenEnum;

import static parser.Parser.*;

class Conditional {
    static void C() {
        tabs(++level); writer.println("C");

        tabs(level + 1); writer.println(look);
        match();

        Pair pair = Condition.C1();
        int c1t = pair.getKey();
        int c1f = pair.getValue();
        backPatch(c1t, Integer.toString(n));
        quadruples.get(c1t - 1).o3 = Integer.toString(n);

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
            quadruples.add(new Quadruple(Quadruple.OPCODE_GOTO));

            backPatch(f, Integer.toString(n));
            quadruples.get(f - 1).o1 = Integer.toString(n);

            B();

            backPatch(next, Integer.toString(n));
            quadruples.get(next - 1).o1 = Integer.toString(n);
        }
        else {
            tabs(level + 1); writer.println("null");

            backPatch(f, Integer.toString(n));
            quadruples.get(f - 1).o1 = Integer.toString(n);
        }

        level--;
    }
}
