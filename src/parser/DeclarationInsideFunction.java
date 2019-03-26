package parser;

import classes.Identifier;

import static parser.Parser.*;

class DeclarationInsideFunction {
    static void D11() {
        tabs(++level); writer.println("D''");

        Identifier.Type type = (Identifier.Type) look.token_type;

        DeclarationOutsideFunction.D1();

        DeclarationOutsideFunction.O11(type);

        level--;
    }
}
