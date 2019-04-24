package classes;

public class Quadruple {
    public int opcode;
    public Object o1 = null;
    public Object o2 = null;
    public Object o3 = null;

    public Quadruple(int opcode) {
        this.opcode = opcode;
    }

    public Quadruple(int opcode, Object o1) {
        this.opcode = opcode;
        this.o1 = o1;
    }

    public Quadruple(int opcode, Object o1, Object o2) {
        this.opcode = opcode;
        this.o1 = o1;
        this.o2 = o2;
    }

    public Quadruple(int opcode, Object o1, Object o2, Object o3) {
        this.opcode = opcode;
        this.o1 = o1;
        this.o2 = o2;
        this.o3 = o3;
    }

    @Override
    public String toString() {
        String string = Integer.toString(opcode) + '\t' + o1;
        if(o2 != null) {
            string += "\t" + o2;
            if(o3 != null)
                string += "\t" + o3;
        }
        return string;
    }

    public static class Pair {
        public byte type;
        public Object value;

        public Pair(byte type, Object value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() { return Byte.toString(type) + ',' + value.toString(); }

        public static final byte TYPE_ADDR_INT = 0;
        public static final byte TYPE_ADDR_CL = 1;
        public static final byte TYPE_INT = 2;
        public static final byte TYPE_CL = 3;
        public static final byte TYPE_STR = 4;
    }

    public static final int OPCODE_ADD = 0;
    public static final int OPCODE_SUB = 1;
    public static final int OPCODE_MUL = 2;
    public static final int OPCODE_DIV = 3;
    public static final int OPCODE_GOTO = 4;
    public static final int OPCODE_IN = 5;
    public static final int OPCODE_OUT = 6;
    public static final int OPCODE_ASSIGN = 7;
    public static final int OPCODE_GT = 8;
    public static final int OPCODE_GE = 9;
    public static final int OPCODE_LT = 10;
    public static final int OPCODE_LE = 11;
    public static final int OPCODE_EQ = 12;
    public static final int OPCODE_NE = 13;
    public static final int OPCODE_CALL = 14;
    public static final int OPCODE_PARM = 15;
    public static final int OPCODE_RET = 16;
}
