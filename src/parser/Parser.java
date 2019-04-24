package parser;

import classes.*;
import vm.VM;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import static Lex.Lex.getCurrentLineNo;

public class Parser {
    static ArrayList<Token> tokens;
    static HashMap<String, Identifier> identifiers;

    static Token look;
    static int index = 0;
    private static int totalTokens = 0;

    static String code;

    static int level = -1;
    static PrintWriter writer = null;


    private static String randomIdPlaceholder = "t";
    private static int idsGenerated = 0;
    private static ArrayList<String> threeAddressCode = new ArrayList<>();

    static HashMap<String, Identifier> translatorIdentifiers = new HashMap<>();

    static int nextFreeMemoryAddress = 0;
    static int n = 1;


    static ArrayList<Quadruple> quadruples = new ArrayList<>();


    public static void Parse(ArrayList<Token> tokens, HashMap<String, Identifier> identifiers, String code) {
        Parser.tokens = tokens;
        Parser.identifiers = identifiers;
        Parser.code = code;

        if(!Parser.tokens.isEmpty()) {
            totalTokens = Parser.tokens.size();
            match();

            try {
                writer = new PrintWriter("parsetree.txt");
                S();
                writer.close();

                writer = new PrintWriter("parser-symboltable.txt");
                PrintWriter writer1 = new PrintWriter("translator-symboltable.txt");
                writer.println("Identifier\tType");
                writer1.println("Identifier\tType\tRelative-Address");
                for (String id : identifiers.keySet()) {
                    Identifier identifier = identifiers.get(id);
                    writer.println(String.format("%s\t%s", id, identifier.type));
                    if(!identifier.type.equals(Identifier.Type.FUN))
                        writer1.println(String.format("%s\t%s\t%s", id, identifier.type, identifier.memoryPosition));
//                    else
//                        writer1.println(String.format("%s\t%s\t%s\t%s", id, identifier.type, identifier.memoryPosition, ((Function) identifier).returnType));
                }
                for(String id : translatorIdentifiers.keySet()) {
                    Identifier identifier = translatorIdentifiers.get(id);
                    writer1.println(String.format("%s\t%s\t%s", id, identifier.type, identifier.memoryPosition));
                }
                writer.close();
                writer1.close();

                Path path = Paths.get("tac.txt");
                Files.write(path, threeAddressCode, Charset.defaultCharset());

                writer = new PrintWriter("mc.txt");
                for(Quadruple quadruple : quadruples)
                    writer.println(quadruple);
                writer.close();


//                System.out.println("Parse tree created successfully!");
//                System.out.println("Three address code create successfully!");
//                System.out.println("Machine Code create successfully!");

                VM.vm(quadruples);
            }
            catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void S() {
        tabs(++level); writer.println("S");

        if(look == null) {
            tabs(level + 1);
            writer.println("null");
        }
        else {
            Object token_type = look.token_type;

            if(token_type instanceof Identifier.Type) {
                DeclarationOutsideFunction.D();

            }
            else if(token_type.equals(TokenEnum.WHILE)) {
                Loop.L();
            }
            else if(token_type.equals(TokenEnum.IF)) {
                Conditional.C();
            }
            else if(token_type.equals(TokenEnum.IN)) {
                In.I();
            }
            else if(token_type.equals(TokenEnum.OUT)) {
                Out.O();
            }
            else if(token_type.equals(TokenEnum.ID)) {
                AssignmentOrFunctionCall.A();
            }
            else {
                System.out.println(String.format("line %d: unexpected token", getCurrentLineNo(code, look.startingIndex)));
                System.exit(0);
            }

            S();
        }
        level--;
    }

    private static void S1() {
        tabs(++level); writer.println("S'");

        if(look == null) {
            tabs(level + 1); writer.println("null");
        }
        else {
            Object token_type = look.token_type;

            if(token_type instanceof Identifier.Type) {
                DeclarationInsideFunction.D11();

            }
            else if(token_type.equals(TokenEnum.WHILE)) {
                Loop.L();
            }
            else if(token_type.equals(TokenEnum.IF)) {
                Conditional.C();
            }
            else if(token_type.equals(TokenEnum.RET)) {
                Return.R();
            }
            else if(token_type.equals(TokenEnum.IN)) {
                In.I();
            }
            else if(token_type.equals(TokenEnum.OUT)) {
                Out.O();
            }
            else if(token_type.equals(TokenEnum.ID)) {
                AssignmentOrFunctionCall.A();
            }
            else {
                tabs(level + 1); writer.println("null");
                level--;
                return;
            }

            S1();
        }

        level--;
    }



    static void B() {
        tabs(++level); writer.println("B");

        Token token = CharacterChecks.checkCharacter('{');
        tabs(level + 1); writer.println(token);

        Parser.S1();

        token = CharacterChecks.checkCharacter('}');
        tabs(level + 1); writer.println(token);

        level--;
    }

    static void match() { if(index < totalTokens) look = tokens.get(index++); else look = null; }

    static void tabs(int amount) { for(int i = 0; i < amount; i++) writer.print('\t'); }

    static void emit(String... strings) {
        threeAddressCode.add(String.join(" ", strings));
        n++;
    }

    static void backPatch(int lineNo, String patch) {
        lineNo--;
        threeAddressCode.set(lineNo, threeAddressCode.get(lineNo) + " " + patch);
    }

    static String newTemp(Identifier.Type identifierType) {
        String id = randomIdPlaceholder + ++idsGenerated;

        Identifier identifier = new Identifier();
        identifier.type = identifierType;
        identifier.memoryPosition = nextFreeMemoryAddress;
        if(identifierType == Identifier.Type.INT)
            nextFreeMemoryAddress += 4;
        else if(identifierType == Identifier.Type.CHAR)
            nextFreeMemoryAddress += 1;
        translatorIdentifiers.put(id, identifier);

        return id;
    }

    static Quadruple.Pair getPair(String str) {
        if(identifiers.containsKey(str) || translatorIdentifiers.containsKey(str)) {
            Identifier identifier = identifiers.getOrDefault(str, translatorIdentifiers.get(str));
            return new Quadruple.Pair(identifier.type == Identifier.Type.INT ? Quadruple.Pair.TYPE_ADDR_INT : Quadruple.Pair.TYPE_ADDR_CL, identifier.memoryPosition);
        }
        else if(str.charAt(0) == '\'')
            return new Quadruple.Pair(Quadruple.Pair.TYPE_CL, (int) str.charAt(1));
        else
            return new Quadruple.Pair(Quadruple.Pair.TYPE_INT, str);
    }
}