import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Assembler {
    ArrayList<String> instructionArray = new ArrayList<>();
    String instructionHexFormat;
    FileWriter writer;

    enum  nzpValues{
        BEQ("010"),
        BGT("001"),
        BLT("100"),
        BGE("011"),
        BLE("110");

        private String nzp;

        nzpValues(String opcode) {
            this.nzp = opcode;
        }

        public String getNzpValue() {
            return nzp;
        }
    }

    enum opcodes
    {
        AND("0010"),
        ANDI("0011"),
        OR("0100"),
        ORI("0101"),
        ADD("0110"),
        ADDI("0111"),
        XOR("1000"),
        XORI("1001"),
        JUMP("1010"),
        LD("1011"),
        ST("1101"),
        BEQ("1110"),
        BGT("1110"),
        BLT("1110"),
        BGE("1110"),
        BLE("1110");

        private String opcode;

        opcodes(String opcode) {
            this.opcode = opcode;
        }

        public String getOpcode() {
            return opcode;
        }
    }

    void run(String filename){
        readInstructions(filename);
        anlyzeInstructrionsAndWriteToHexFile();
    }

    void analyzeInstruction(String instruction){
        Scanner scan = new Scanner(instruction);
        String operator = scan.next();
        String arguments = scan.next();
        switch (operator){
            case "ADD":
            case "OR" :
            case "AND":
            case "XOR":
                instructionHexFormat=splitInForm1(operator,arguments);
                break;
            case "ADDI":
            case "ORI":
            case "ANDI":
            case "XORI":
                instructionHexFormat=splitInForm2(operator,arguments);
                break;
            case "JUMP":
                instructionHexFormat=splitInJumpForm(operator,arguments);
                break;
            case "LD":
            case "ST":
                instructionHexFormat=splitInForm3(operator,arguments);
                break;
            case "BEQ":
            case "BGT":
            case "BLT":
            case "BGE":
            case "BLE":
                instructionHexFormat=splitInForm4(operator,arguments);
                break;
        }
    }

    String splitInForm1(String operator,String arguments){
        String binary=opcodes.valueOf(operator).getOpcode();
        String operands[] = arguments.split(",");
        for (String operand: operands){
            binary+=registerToBinary(operand);
        }
        binary+="00";
        int decimal = Integer.parseInt(binary,2);
        String hexFormat=Integer.toHexString(decimal);

        return hexFormat;
    }

    String splitInForm2(String operator,String arguments){
        String binary=opcodes.valueOf(operator).getOpcode();
        String operands[] = arguments.split(",");
        for (int i = 0; i <operands.length-1 ; i++) {
            binary+=registerToBinary(operands[i]);
        }
        int imm6 = Integer.parseInt(operands[2]);
        String binaryImm = Integer.toBinaryString(imm6);
        binary+=String.format("%32s", binaryImm).replace(" ", "0").substring(26,32);

        int decimal = Integer.parseInt(binary,2);
        String hexFormat = Integer.toHexString(decimal);

        return hexFormat;
    }

    String splitInJumpForm(String operator, String argument){
        String binary=opcodes.valueOf(operator).getOpcode();
        int address14 = Integer.parseInt(argument);
        String binaryAddress14 = Integer.toBinaryString(address14);
        binary+=String.format("%32s", binaryAddress14).replace(" ", "0").substring(18,32);

        int decimal = Integer.parseInt(binary,2);
        String hexFormat = Integer.toHexString(decimal);

        return hexFormat;
    }

    String splitInForm3(String operator,String arguments){
        String binary=opcodes.valueOf(operator).getOpcode();
        String operands[] = arguments.split(",");

        binary+=registerToBinary(operands[0]);
        int address10 = Integer.parseInt(operands[1]);
        String binaryAddress10 = Integer.toBinaryString(address10);
        binary+=String.format("%32s", binaryAddress10).replace(" ", "0").substring(22,32);

        int decimal = Integer.parseInt(binary,2);
        String hexFormat = Integer.toHexString(decimal);

        return hexFormat;

    }

    String splitInForm4(String operator,String arguments){
        String binary=opcodes.valueOf(operator).getOpcode();
        String operands[] = arguments.split(",");

        for (int i = 0; i <operands.length-1 ; i++) {
            binary+=registerToBinary(operands[i]);
        }

        binary+=nzpValues.valueOf(operator).getNzpValue();


        int address3 = Integer.parseInt(operands[2]);
        String binaryAddress3 = Integer.toBinaryString(address3);

        binary+=String.format("%32s", binaryAddress3).replace(" ", "0").substring(29,32);


        int decimal = Integer.parseInt(binary,2);
        String hexFormat = Integer.toHexString(decimal);

        return hexFormat;

    }

    String registerToBinary(String register){
       String number = register.replaceAll("\\D+","");
       String binaryString = Integer.toBinaryString(Integer.parseInt(number));
       binaryString=String.format("%4s", binaryString).replace(" ", "0");
       return binaryString;
    }

    void readInstructions(String filename){
        File file = new File(filename);
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (sc.hasNextLine())
            instructionArray.add(sc.nextLine());
    }

    void anlyzeInstructrionsAndWriteToHexFile(){
        try{
            writer = new FileWriter("output.hex", false);

            writer.write("v2.0 raw\n");
            for (int i = 0; i < instructionArray.size(); i++) {
                analyzeInstruction(instructionArray.get(i));
                writer.write(instructionHexFormat);
                writer.write(" ");
            }
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
