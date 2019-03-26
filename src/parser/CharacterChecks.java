package parser;

import classes.Identifier;
import classes.Token;
import classes.TokenEnum;

import static parser.Parser.look;
import static parser.Parser.identifiers;
import static parser.Parser.match;

class CharacterChecks {
    static Token checkCharacter(char c) {
        if (look == null || !look.token_type.equals(c)) {
            Errors.characterMissing(c);
        }

        Token token = look;

        match();

        return token;
    }

    static Token checkIdentifier(Identifier.Type type) {
        if(look == null || !look.token_type.equals(TokenEnum.ID))
            Errors.identifierMissing();

        Token token = look;

        Identifier identifier = identifiers.get(token.lexeme.toString());
//        if (identifier.type != null)
//            Errors.identifierAlreadyDefined();

        identifier.type = type;

        match();

        if (look.token_type.equals('('))
            identifier.type = Identifier.Type.FUN;

        return token;
    }
}
