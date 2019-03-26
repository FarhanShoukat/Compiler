package classes;

public class Token {
    //(token_type, lexeme)

    public final Object token_type;
    public final Object lexeme;
    public final int startingIndex;

    public Token(Object token_type, int startingIndex) {
        this.token_type = token_type;
        lexeme = null;
        this.startingIndex = startingIndex;
    }

    public Token(Object token_type, Object lexeme, int startingIndex) {
        this.token_type = token_type;
        this.lexeme = lexeme;
        this.startingIndex = startingIndex;
    }

    @Override
    public String toString() {
        return String.format("(%s,%s)", token_type.toString(), lexeme != null ? lexeme.toString() : "^");
    }
}