package vm;

import classes.Quadruple;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Scanner;

public class VM {
    private static final byte[] ds = new byte[1000];

    public static void vm(ArrayList<Quadruple> quadruples) {
        int length = quadruples.size();
        Scanner scanner = new Scanner(System.in);
        for(int pc = 0; pc < length; pc++) {
            Quadruple quadruple = quadruples.get(pc);
            switch(quadruple.opcode) {
                case Quadruple.OPCODE_ADD: {
                    int first = pairToInt((Quadruple.Pair) quadruple.o1);
                    int second = pairToInt((Quadruple.Pair) quadruple.o2);
                    setValue((Quadruple.Pair) quadruple.o3,first + second);
                    break;
                }
                case Quadruple.OPCODE_SUB: {
                    int first = pairToInt((Quadruple.Pair) quadruple.o1);
                    int second = pairToInt((Quadruple.Pair) quadruple.o2);
                    setValue((Quadruple.Pair) quadruple.o3,first - second);
                    break;
                }
                case Quadruple.OPCODE_MUL: {
                    int first = pairToInt((Quadruple.Pair) quadruple.o1);
                    int second = pairToInt((Quadruple.Pair) quadruple.o2);
                    setValue((Quadruple.Pair) quadruple.o3,first * second);
                    break;
                }
                case Quadruple.OPCODE_DIV: {
                    int first = pairToInt((Quadruple.Pair) quadruple.o1);
                    int second = pairToInt((Quadruple.Pair) quadruple.o2);
                    setValue((Quadruple.Pair) quadruple.o3,first / second);
                    break;
                }
                case Quadruple.OPCODE_GOTO: {
                    pc = Integer.parseInt((String) quadruple.o1) - 2;
                    break;
                }
                case Quadruple.OPCODE_IN: {
                    String string = scanner.next();
                    int temp;
                    try { temp = Integer.parseInt(string); }
                    catch(NumberFormatException ignored) { temp = string.charAt(0); }
                    setValue((Quadruple.Pair) quadruple.o1, temp);
                    break;
                }
                case Quadruple.OPCODE_OUT: {
                    System.out.println(getPairValue((Quadruple.Pair) quadruple.o1));
                    break;
                }
                case Quadruple.OPCODE_ASSIGN: {
                    setValue((Quadruple.Pair) quadruple.o1, pairToInt((Quadruple.Pair) quadruple.o2));
                    break;
                }
                case Quadruple.OPCODE_GT: {
                    int first = pairToInt((Quadruple.Pair) quadruple.o1);
                    int second = pairToInt((Quadruple.Pair) quadruple.o2);
                    if(first > second)
                        pc = Integer.parseInt((String) quadruple.o3) - 2;
                    break;
                }
                case Quadruple.OPCODE_GE: {
                    int first = pairToInt((Quadruple.Pair) quadruple.o1);
                    int second = pairToInt((Quadruple.Pair) quadruple.o2);
                    if(first >= second)
                        pc = Integer.parseInt((String) quadruple.o3) - 2;
                    break;
                }
                case Quadruple.OPCODE_LT: {
                    int first = pairToInt((Quadruple.Pair) quadruple.o1);
                    int second = pairToInt((Quadruple.Pair) quadruple.o2);
                    if(first < second)
                        pc = Integer.parseInt((String) quadruple.o3) - 2;
                    break;
                }
                case Quadruple.OPCODE_LE: {
                    int first = pairToInt((Quadruple.Pair) quadruple.o1);
                    int second = pairToInt((Quadruple.Pair) quadruple.o2);
                    if(first <= second)
                        pc = Integer.parseInt((String) quadruple.o3) - 2;
                    break;
                }
                case Quadruple.OPCODE_NE: {
                    int first = pairToInt((Quadruple.Pair) quadruple.o1);
                    int second = pairToInt((Quadruple.Pair) quadruple.o2);
                    if(first != second)
                        pc = Integer.parseInt((String) quadruple.o3) - 2;
                    break;
                }
                case Quadruple.OPCODE_EQ: {
                    int first = pairToInt((Quadruple.Pair) quadruple.o1);
                    int second = pairToInt((Quadruple.Pair) quadruple.o2);
                    if(first == second)
                        pc = Integer.parseInt((String) quadruple.o3) - 2;
                    break;
                }
                case Quadruple.OPCODE_CALL:
                case Quadruple.OPCODE_PARM:
                case Quadruple.OPCODE_RET: {
                    System.out.println("Functions are not supported yet.");
                    System.exit(0);
                }
                default: {
                    System.out.println("Unrecongnized opcode found:" + quadruple.opcode);
                }
            }
        }
    }

    private static int pairToInt(Quadruple.Pair pair) {
        Object o = getPairValue(pair);
        return o instanceof Integer ? (int) o : (int) (char) o;
    }

    private static Object getPairValue(Quadruple.Pair pair) {
        switch(pair.type) {
            case Quadruple.Pair.TYPE_ADDR_CL: return (char) ds[(int) pair.value];
            case Quadruple.Pair.TYPE_ADDR_INT: {
                int index = (int) pair.value;
                byte[] temp = new byte[]{ds[index], ds[index + 1], ds[index + 2], ds[index + 3]};
                return ByteBuffer.wrap(temp).order(ByteOrder.LITTLE_ENDIAN).getInt();
            }
            case Quadruple.Pair.TYPE_CL: return (char) (int) pair.value;
            case Quadruple.Pair.TYPE_INT: return Integer.parseInt(pair.value.toString());
            case Quadruple.Pair.TYPE_STR: return pair.value;
        }
        return -1;
    }

    private static void setValue(Quadruple.Pair pair, int value) {
        byte[] temp = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array();
        int index = (int) pair.value;
        if(pair.type == Quadruple.Pair.TYPE_ADDR_CL) {
            ds[index] = temp[0];
        }
        else if(pair.type == Quadruple.Pair.TYPE_ADDR_INT) {
            System.arraycopy(temp, 0, ds, index, 4);
        }
    }
}
