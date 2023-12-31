package cmu.pasta.mu2.instrument;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Describes an instruction call in bytecode using ASM
 *
 * @author Bella Laybourn
 */
public class InstructionCall implements Opcodes {

  public enum CallType {

    //regular instruction
    INSN,
    METHOD_INSN,

    //load constant instruction
    LDC_INSN,
    JUMP_INSN
  }

  //for any insn:
  private final CallType type;

  //byte level operation code
  private final int opcode;

  //for methodInsn:
  private String owner;
  private String name;
  private String descriptor;
  private boolean isInterface;

  //for ldcInsn:
  private Object argument;

  /**
   * use for default insn
   */
  public InstructionCall(int op) {
    type = CallType.INSN;
    opcode = op;
  }

  /**
   * use if methodInsn
   */
  public InstructionCall(int op, String ow, String n, String d, boolean i) {
    type = CallType.METHOD_INSN;
    opcode = op;
    owner = ow;
    name = n;
    descriptor = d;
    isInterface = i;
  }

  /**
   * use if ldcInsn
   */
  public InstructionCall(int op, Object arg) {
    if (op == Opcodes.LDC) {
      type = CallType.LDC_INSN;
      opcode = op;
      argument = arg;
    } else {
      type = CallType.JUMP_INSN;
      opcode = op;
    }
  }

  public void call(MethodVisitor mv, Label l) {
    switch (type) {
      case INSN:
        mv.visitInsn(opcode);
        break;
      case METHOD_INSN:
        mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        break;
      case LDC_INSN:
        mv.visitLdcInsn(argument);
        break;
      case JUMP_INSN:
        mv.visitJumpInsn(opcode, l);
    }
  }

  @Override
  public String toString() {
    return "InstructionCall " + type + " - " + opcode;
  }
}
