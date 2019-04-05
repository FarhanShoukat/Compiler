package parser;

import classes.Token;
import classes.TokenEnum;

import static parser.Parser.*;

class In {
    static void I() {
        tabs(++level); writer.println("I");

        tabs(level + 1); writer.println(look);
        match();

        if(look == null || !look.token_type.equals(TokenEnum.ID))
            Errors.identifierMissing();
//        else if(identifiers.get(look.lexeme.toString()).type == null)
//            Errors.identifierNotDefined();

        String idLex = look.lexeme.toString();
        tabs(level + 1); writer.println(look);
        match();

        Token token = CharacterChecks.checkCharacter(';');
        tabs(level + 1); writer.println(token);

        emit("in", idLex);

        level--;
    }
}
