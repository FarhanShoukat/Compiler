package parser;

import classes.Function;
import classes.Identifier;
import classes.Token;
import classes.TokenEnum;

import static parser.Parser.*;

class CharacterChecks {
    static Token checkCharacter(char c) {
        if (look == null || !look.token_type.equals(c)) {
            Errors.characterMissing(c);
        }

        Token token = look;

        match();

        return token;
    }

    static Token checkIdentifier(Identifier.Type type, int size) {
        if(look == null || !look.token_type.equals(TokenEnum.ID))
            Errors.identifierMissing();

        Token token = look;

        Identifier identifier = identifiers.get(token.lexeme.toString());
//        if (identifier.type != null)
//            Errors.identifierAlreadyDefined();

        match();

        if (look.token_type.equals('(')) {
            identifier = new Function();
            identifier.type = Identifier.Type.FUN;
            identifier.memoryPosition = n;
            ((Function) identifier).returnType = type;
            identifiers.put(token.lexeme.toString(), identifier);
        }
        else {
            identifier.type = type;
            identifier.memoryPosition = nextFreeMemoryAddress;
            nextFreeMemoryAddress += size;
        }

        return token;
    }
}
