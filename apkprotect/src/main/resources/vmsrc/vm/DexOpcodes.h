
/*
 * Dalvik opcode information.
 *
 * IMPORTANT NOTE: The contents of this file are mostly generated
 * automatically by the opcode-gen tool. Any edits to the generated
 * sections will get wiped out the next time the tool is run.
 *
 * See the file opcode-gen/README.txt for information about updating
 * opcodes and instruction formats.
 */

#ifndef LIBDEX_DEXOPCODES_H_
#define LIBDEX_DEXOPCODES_H_

#include "./include/Common.h"
/*
 * gcc-style inline management -- ensures we have a copy of all functions
 * in the library, so code that links against us will work whether or not
 * it was built with optimizations enabled.
 */
#ifndef _DEX_GEN_INLINES             /* only defined by DexInlines.c */
# define DEX_INLINE extern __inline__
#else
# define DEX_INLINE
#endif

/*
 * kMaxOpcodeValue: the highest possible raw (unpacked) opcode value
 *
 * kNumPackedOpcodes: the highest possible packed opcode value of a
 * valid Dalvik opcode, plus one
 *
 * TODO: Change this once the rest of the code is prepared to deal with
 * extended opcodes.
 */
// BEGIN(libdex-maximum-values); GENERATED AUTOMATICALLY BY opcode-gen
#define kMaxOpcodeValue 0xffff
#define kNumPackedOpcodes 0x100
// END(libdex-maximum-values); GENERATED AUTOMATICALLY BY opcode-gen

/*
 * Switch table and array data signatures are a code unit consisting
 * of "NOP" (0x00) in the low-order byte and a non-zero identifying
 * code in the high-order byte. (A true NOP is 0x0000.)
 */
#define kPackedSwitchSignature  0x0100
#define kSparseSwitchSignature  0x0200
#define kArrayDataSignature     0x0300

/*
 * Enumeration of all Dalvik opcodes, where the enumeration value
 * associated with each is the corresponding packed opcode number.
 * This is different than the opcode value from the Dalvik bytecode
 * spec for opcode values >= 0xff; see dexOpcodeFromCodeUnit() below.
 *
 * A note about the "breakpoint" opcode. This instruction is special,
 * in that it should never be seen by anything but the debug
 * interpreter. During debugging it takes the place of an arbitrary
 * opcode, which means operations like "tell me the opcode width so I
 * can find the next instruction" aren't possible. (This is
 * correctable, but probably not useful.)
 */
enum Opcode {
    OP_NOP     = 0x0,
    OP_NOT_LONG     = 0x1,
    OP_APUT_CHAR     = 0x2,
    OP_MUL_DOUBLE_2ADDR     = 0x3,
    OP_SGET_SHORT     = 0x4,
    OP_XOR_INT_2ADDR     = 0x5,
    OP_IGET_CHAR_QUICK     = 0x6,
    OP_MOVE_RESULT_OBJECT     = 0x7,
    OP_MOVE_RESULT_WIDE     = 0x8,
    OP_INSTANCE_OF     = 0x9,
    OP_RETURN_VOID_NO_BARRIER     = 0xa,
    OP_MOVE_16     = 0xb,
    OP_DIV_INT_2ADDR     = 0xc,
    OP_CONST_METHOD_HANDLE     = 0xd,
    OP_SPUT_CHAR     = 0xe,
    OP_INT_TO_DOUBLE     = 0xf,
    OP_IGET_BYTE_QUICK     = 0x10,
    OP_GOTO_16     = 0x11,
    OP_REM_INT_LIT16     = 0x12,
    OP_SPUT_BOOLEAN     = 0x13,
    OP_INVOKE_VIRTUAL_RANGE     = 0x14,
    OP_ADD_DOUBLE     = 0x15,
    OP_FILLED_NEW_ARRAY_RANGE     = 0x16,
    OP_RETURN_WIDE     = 0x17,
    OP_APUT_OBJECT     = 0x18,
    OP_SPARSE_SWITCH     = 0x19,
    OP_IPUT_WIDE     = 0x1a,
    OP_INVOKE_SUPER_RANGE     = 0x1b,
    OP_THROW     = 0x1c,
    OP_INVOKE_INTERFACE_RANGE     = 0x1d,
    OP_CONST_HIGH16     = 0x1e,
    OP_MOVE_EXCEPTION     = 0x1f,
    OP_ARRAY_LENGTH     = 0x20,
    OP_FLOAT_TO_INT     = 0x21,
    OP_CONST_WIDE_HIGH16     = 0x22,
    OP_MOVE_FROM16     = 0x23,
    OP_IGET_SHORT_QUICK     = 0x24,
    OP_SGET_BOOLEAN     = 0x25,
    OP_SUB_LONG_2ADDR     = 0x26,
    OP_OR_INT_LIT8     = 0x27,
    OP_INT_TO_CHAR     = 0x28,
    OP_GOTO     = 0x29,
    OP_CHECK_CAST     = 0x2a,
    OP_ADD_DOUBLE_2ADDR     = 0x2b,
    OP_IF_NEZ     = 0x2c,
    OP_MUL_INT     = 0x2d,
    OP_IPUT_BYTE_QUICK     = 0x2e,
    OP_SPUT_OBJECT     = 0x2f,
    OP_XOR_LONG     = 0x30,
    OP_APUT_SHORT     = 0x31,
    OP_RSUB_INT_LIT8     = 0x32,
    OP_NEG_FLOAT     = 0x33,
    OP_IPUT_OBJECT     = 0x34,
    OP_SUB_INT     = 0x35,
    OP_ADD_INT_LIT16     = 0x36,
    OP_ADD_INT     = 0x37,
    OP_IF_GEZ     = 0x38,
    OP_INT_TO_BYTE     = 0x39,
    OP_USHR_LONG_2ADDR     = 0x3a,
    OP_IF_LE     = 0x3b,
    OP_AGET_BYTE     = 0x3c,
    OP_IPUT_CHAR     = 0x3d,
    OP_UNUSED_3E     = 0x3e,
    OP_UNUSED_3F     = 0x3f,
    OP_UNUSED_40     = 0x40,
    OP_UNUSED_41     = 0x41,
    OP_UNUSED_42     = 0x42,
    OP_UNUSED_43     = 0x43,
    OP_SGET_CHAR     = 0x44,
    OP_SPUT_WIDE     = 0x45,
    OP_SUB_DOUBLE_2ADDR     = 0x46,
    OP_ADD_FLOAT_2ADDR     = 0x47,
    OP_CMPG_DOUBLE     = 0x48,
    OP_IGET_OBJECT     = 0x49,
    OP_IF_LEZ     = 0x4a,
    OP_SHR_LONG_2ADDR     = 0x4b,
    OP_DIV_INT_LIT16     = 0x4c,
    OP_CONST_16     = 0x4d,
    OP_USHR_INT     = 0x4e,
    OP_NEG_DOUBLE     = 0x4f,
    OP_AGET_OBJECT     = 0x50,
    OP_CMPG_FLOAT     = 0x51,
    OP_LONG_TO_DOUBLE     = 0x52,
    OP_AND_LONG_2ADDR     = 0x53,
    OP_IGET_QUICK     = 0x54,
    OP_XOR_INT     = 0x55,
    OP_CMP_LONG     = 0x56,
    OP_IF_GT     = 0x57,
    OP_CMPL_DOUBLE     = 0x58,
    OP_CONST_METHOD_TYPE     = 0x59,
    OP_SUB_LONG     = 0x5a,
    OP_CMPL_FLOAT     = 0x5b,
    OP_MOVE_WIDE     = 0x5c,
    OP_NOT_INT     = 0x5d,
    OP_DIV_INT     = 0x5e,
    OP_CONST_CLASS     = 0x5f,
    OP_NEG_INT     = 0x60,
    OP_IPUT_OBJECT_QUICK     = 0x61,
    OP_SHL_LONG     = 0x62,
    OP_INT_TO_SHORT     = 0x63,
    OP_IPUT_CHAR_QUICK     = 0x64,
    OP_IF_GE     = 0x65,
    OP_AGET_SHORT     = 0x66,
    OP_FLOAT_TO_DOUBLE     = 0x67,
    OP_IPUT_QUICK     = 0x68,
    OP_REM_DOUBLE     = 0x69,
    OP_ADD_INT_2ADDR     = 0x6a,
    OP_FLOAT_TO_LONG     = 0x6b,
    OP_LONG_TO_FLOAT     = 0x6c,
    OP_MUL_FLOAT_2ADDR     = 0x6d,
    OP_SHL_INT     = 0x6e,
    OP_MOVE_RESULT     = 0x6f,
    OP_XOR_INT_LIT8     = 0x70,
    OP_DOUBLE_TO_FLOAT     = 0x71,
    OP_INVOKE_DIRECT_RANGE     = 0x72,
    OP_USHR_LONG     = 0x73,
    OP_MONITOR_EXIT     = 0x74,
    OP_RETURN     = 0x75,
    OP_IF_EQ     = 0x76,
    OP_USHR_INT_2ADDR     = 0x77,
    OP_INVOKE_DIRECT     = 0x78,
    OP_UNUSED_79     = 0x79,
    OP_UNUSED_7A     = 0x7a,
    OP_REM_FLOAT     = 0x7b,
    OP_IPUT_BYTE     = 0x7c,
    OP_IPUT_WIDE_QUICK     = 0x7d,
    OP_ADD_INT_LIT8     = 0x7e,
    OP_INVOKE_VIRTUAL_QUICK     = 0x7f,
    OP_MOVE_OBJECT_16     = 0x80,
    OP_MUL_LONG_2ADDR     = 0x81,
    OP_OR_INT     = 0x82,
    OP_IPUT_SHORT_QUICK     = 0x83,
    OP_OR_LONG_2ADDR     = 0x84,
    OP_CONST_STRING_JUMBO     = 0x85,
    OP_INVOKE_CUSTOM     = 0x86,
    OP_REM_DOUBLE_2ADDR     = 0x87,
    OP_OR_INT_LIT16     = 0x88,
    OP_IF_NE     = 0x89,
    OP_MOVE_OBJECT_FROM16     = 0x8a,
    OP_AGET_CHAR     = 0x8b,
    OP_CONST_4     = 0x8c,
    OP_SHL_LONG_2ADDR     = 0x8d,
    OP_IF_GTZ     = 0x8e,
    OP_SHL_INT_2ADDR     = 0x8f,
    OP_INT_TO_LONG     = 0x90,
    OP_REM_LONG     = 0x91,
    OP_AND_INT_LIT16     = 0x92,
    OP_IGET_WIDE     = 0x93,
    OP_REM_INT     = 0x94,
    OP_ADD_FLOAT     = 0x95,
    OP_REM_LONG_2ADDR     = 0x96,
    OP_REM_INT_2ADDR     = 0x97,
    OP_APUT_BOOLEAN     = 0x98,
    OP_LONG_TO_INT     = 0x99,
    OP_IGET_BOOLEAN     = 0x9a,
    OP_SUB_INT_2ADDR     = 0x9b,
    OP_ADD_LONG     = 0x9c,
    OP_SGET_OBJECT     = 0x9d,
    OP_IGET_BYTE     = 0x9e,
    OP_OR_INT_2ADDR     = 0x9f,
    OP_APUT_WIDE     = 0xa0,
    OP_INVOKE_INTERFACE     = 0xa1,
    OP_MUL_INT_2ADDR     = 0xa2,
    OP_DIV_DOUBLE     = 0xa3,
    OP_SUB_FLOAT_2ADDR     = 0xa4,
    OP_MUL_LONG     = 0xa5,
    OP_INVOKE_POLYMORPHIC_RANGE     = 0xa6,
    OP_INVOKE_SUPER     = 0xa7,
    OP_IF_EQZ     = 0xa8,
    OP_MOVE_OBJECT     = 0xa9,
    OP_DIV_DOUBLE_2ADDR     = 0xaa,
    OP_INVOKE_STATIC     = 0xab,
    OP_APUT     = 0xac,
    OP_SHR_LONG     = 0xad,
    OP_CONST_WIDE_16     = 0xae,
    OP_AND_INT     = 0xaf,
    OP_MOVE     = 0xb0,
    OP_SPUT_BYTE     = 0xb1,
    OP_INT_TO_FLOAT     = 0xb2,
    OP_SUB_DOUBLE     = 0xb3,
    OP_IF_LTZ     = 0xb4,
    OP_INVOKE_STATIC_RANGE     = 0xb5,
    OP_IGET_OBJECT_QUICK     = 0xb6,
    OP_DIV_FLOAT_2ADDR     = 0xb7,
    OP_GOTO_32     = 0xb8,
    OP_DIV_LONG     = 0xb9,
    OP_MUL_INT_LIT16     = 0xba,
    OP_SGET_WIDE     = 0xbb,
    OP_DIV_FLOAT     = 0xbc,
    OP_USHR_INT_LIT8     = 0xbd,
    OP_SGET_BYTE     = 0xbe,
    OP_CONST_WIDE     = 0xbf,
    OP_OR_LONG     = 0xc0,
    OP_MONITOR_ENTER     = 0xc1,
    OP_AGET_WIDE     = 0xc2,
    OP_INVOKE_CUSTOM_RANGE     = 0xc3,
    OP_AGET     = 0xc4,
    OP_SHR_INT_LIT8     = 0xc5,
    OP_NEW_ARRAY     = 0xc6,
    OP_IPUT_BOOLEAN_QUICK     = 0xc7,
    OP_DOUBLE_TO_INT     = 0xc8,
    OP_RETURN_VOID     = 0xc9,
    OP_NEW_INSTANCE     = 0xca,
    OP_INVOKE_VIRTUAL     = 0xcb,
    OP_DOUBLE_TO_LONG     = 0xcc,
    OP_SHL_INT_LIT8     = 0xcd,
    OP_DIV_INT_LIT8     = 0xce,
    OP_CONST_STRING     = 0xcf,
    OP_ADD_LONG_2ADDR     = 0xd0,
    OP_INVOKE_POLYMORPHIC     = 0xd1,
    OP_AGET_BOOLEAN     = 0xd2,
    OP_IPUT_BOOLEAN     = 0xd3,
    OP_MUL_FLOAT     = 0xd4,
    OP_AND_LONG     = 0xd5,
    OP_IGET_CHAR     = 0xd6,
    OP_MUL_DOUBLE     = 0xd7,
    OP_DIV_LONG_2ADDR     = 0xd8,
    OP_SUB_FLOAT     = 0xd9,
    OP_IGET_BOOLEAN_QUICK     = 0xda,
    OP_MUL_INT_LIT8     = 0xdb,
    OP_MOVE_WIDE_16     = 0xdc,
    OP_IPUT     = 0xdd,
    OP_SHR_INT     = 0xde,
    OP_IPUT_SHORT     = 0xdf,
    OP_IF_LT     = 0xe0,
    OP_AND_INT_LIT8     = 0xe1,
    OP_IGET_WIDE_QUICK     = 0xe2,
    OP_FILLED_NEW_ARRAY     = 0xe3,
    OP_CONST     = 0xe4,
    OP_RSUB_INT     = 0xe5,
    OP_MOVE_WIDE_FROM16     = 0xe6,
    OP_APUT_BYTE     = 0xe7,
    OP_IGET     = 0xe8,
    OP_REM_FLOAT_2ADDR     = 0xe9,
    OP_SPUT_SHORT     = 0xea,
    OP_PACKED_SWITCH     = 0xeb,
    OP_IGET_SHORT     = 0xec,
    OP_XOR_INT_LIT16     = 0xed,
    OP_AND_INT_2ADDR     = 0xee,
    OP_FILL_ARRAY_DATA     = 0xef,
    OP_SGET     = 0xf0,
    OP_NEG_LONG     = 0xf1,
    OP_INVOKE_VIRTUAL_QUICK_RANGE     = 0xf2,
    OP_UNUSED_F3     = 0xf3,
    OP_UNUSED_F4     = 0xf4,
    OP_UNUSED_F5     = 0xf5,
    OP_UNUSED_F6     = 0xf6,
    OP_UNUSED_F7     = 0xf7,
    OP_UNUSED_F8     = 0xf8,
    OP_UNUSED_F9     = 0xf9,
    OP_XOR_LONG_2ADDR     = 0xfa,
    OP_CONST_WIDE_32     = 0xfb,
    OP_SHR_INT_2ADDR     = 0xfc,
    OP_SPUT     = 0xfd,
    OP_RETURN_OBJECT     = 0xfe,
    OP_REM_INT_LIT8     = 0xff,
};


/*
 * Macro used to generate a computed goto table for use in implementing
 * an interpreter in C.
 */
#define DEFINE_GOTO_TABLE(_name) \
    static const void* _name[kNumPackedOpcodes] = {        \
    H(OP_NOP),                                                            \
    H(OP_NOT_LONG),                                                            \
    H(OP_APUT_CHAR),                                                            \
    H(OP_MUL_DOUBLE_2ADDR),                                                            \
    H(OP_SGET_SHORT),                                                            \
    H(OP_XOR_INT_2ADDR),                                                            \
    H(OP_IGET_CHAR_QUICK),                                                            \
    H(OP_MOVE_RESULT_OBJECT),                                                            \
    H(OP_MOVE_RESULT_WIDE),                                                            \
    H(OP_INSTANCE_OF),                                                            \
    H(OP_RETURN_VOID_NO_BARRIER),                                                            \
    H(OP_MOVE_16),                                                            \
    H(OP_DIV_INT_2ADDR),                                                            \
    H(OP_CONST_METHOD_HANDLE),                                                            \
    H(OP_SPUT_CHAR),                                                            \
    H(OP_INT_TO_DOUBLE),                                                            \
    H(OP_IGET_BYTE_QUICK),                                                            \
    H(OP_GOTO_16),                                                            \
    H(OP_REM_INT_LIT16),                                                            \
    H(OP_SPUT_BOOLEAN),                                                            \
    H(OP_INVOKE_VIRTUAL_RANGE),                                                            \
    H(OP_ADD_DOUBLE),                                                            \
    H(OP_FILLED_NEW_ARRAY_RANGE),                                                            \
    H(OP_RETURN_WIDE),                                                            \
    H(OP_APUT_OBJECT),                                                            \
    H(OP_SPARSE_SWITCH),                                                            \
    H(OP_IPUT_WIDE),                                                            \
    H(OP_INVOKE_SUPER_RANGE),                                                            \
    H(OP_THROW),                                                            \
    H(OP_INVOKE_INTERFACE_RANGE),                                                            \
    H(OP_CONST_HIGH16),                                                            \
    H(OP_MOVE_EXCEPTION),                                                            \
    H(OP_ARRAY_LENGTH),                                                            \
    H(OP_FLOAT_TO_INT),                                                            \
    H(OP_CONST_WIDE_HIGH16),                                                            \
    H(OP_MOVE_FROM16),                                                            \
    H(OP_IGET_SHORT_QUICK),                                                            \
    H(OP_SGET_BOOLEAN),                                                            \
    H(OP_SUB_LONG_2ADDR),                                                            \
    H(OP_OR_INT_LIT8),                                                            \
    H(OP_INT_TO_CHAR),                                                            \
    H(OP_GOTO),                                                            \
    H(OP_CHECK_CAST),                                                            \
    H(OP_ADD_DOUBLE_2ADDR),                                                            \
    H(OP_IF_NEZ),                                                            \
    H(OP_MUL_INT),                                                            \
    H(OP_IPUT_BYTE_QUICK),                                                            \
    H(OP_SPUT_OBJECT),                                                            \
    H(OP_XOR_LONG),                                                            \
    H(OP_APUT_SHORT),                                                            \
    H(OP_RSUB_INT_LIT8),                                                            \
    H(OP_NEG_FLOAT),                                                            \
    H(OP_IPUT_OBJECT),                                                            \
    H(OP_SUB_INT),                                                            \
    H(OP_ADD_INT_LIT16),                                                            \
    H(OP_ADD_INT),                                                            \
    H(OP_IF_GEZ),                                                            \
    H(OP_INT_TO_BYTE),                                                            \
    H(OP_USHR_LONG_2ADDR),                                                            \
    H(OP_IF_LE),                                                            \
    H(OP_AGET_BYTE),                                                            \
    H(OP_IPUT_CHAR),                                                            \
    H(OP_UNUSED_3E),                                                            \
    H(OP_UNUSED_3F),                                                            \
    H(OP_UNUSED_40),                                                            \
    H(OP_UNUSED_41),                                                            \
    H(OP_UNUSED_42),                                                            \
    H(OP_UNUSED_43),                                                            \
    H(OP_SGET_CHAR),                                                            \
    H(OP_SPUT_WIDE),                                                            \
    H(OP_SUB_DOUBLE_2ADDR),                                                            \
    H(OP_ADD_FLOAT_2ADDR),                                                            \
    H(OP_CMPG_DOUBLE),                                                            \
    H(OP_IGET_OBJECT),                                                            \
    H(OP_IF_LEZ),                                                            \
    H(OP_SHR_LONG_2ADDR),                                                            \
    H(OP_DIV_INT_LIT16),                                                            \
    H(OP_CONST_16),                                                            \
    H(OP_USHR_INT),                                                            \
    H(OP_NEG_DOUBLE),                                                            \
    H(OP_AGET_OBJECT),                                                            \
    H(OP_CMPG_FLOAT),                                                            \
    H(OP_LONG_TO_DOUBLE),                                                            \
    H(OP_AND_LONG_2ADDR),                                                            \
    H(OP_IGET_QUICK),                                                            \
    H(OP_XOR_INT),                                                            \
    H(OP_CMP_LONG),                                                            \
    H(OP_IF_GT),                                                            \
    H(OP_CMPL_DOUBLE),                                                            \
    H(OP_CONST_METHOD_TYPE),                                                            \
    H(OP_SUB_LONG),                                                            \
    H(OP_CMPL_FLOAT),                                                            \
    H(OP_MOVE_WIDE),                                                            \
    H(OP_NOT_INT),                                                            \
    H(OP_DIV_INT),                                                            \
    H(OP_CONST_CLASS),                                                            \
    H(OP_NEG_INT),                                                            \
    H(OP_IPUT_OBJECT_QUICK),                                                            \
    H(OP_SHL_LONG),                                                            \
    H(OP_INT_TO_SHORT),                                                            \
    H(OP_IPUT_CHAR_QUICK),                                                            \
    H(OP_IF_GE),                                                            \
    H(OP_AGET_SHORT),                                                            \
    H(OP_FLOAT_TO_DOUBLE),                                                            \
    H(OP_IPUT_QUICK),                                                            \
    H(OP_REM_DOUBLE),                                                            \
    H(OP_ADD_INT_2ADDR),                                                            \
    H(OP_FLOAT_TO_LONG),                                                            \
    H(OP_LONG_TO_FLOAT),                                                            \
    H(OP_MUL_FLOAT_2ADDR),                                                            \
    H(OP_SHL_INT),                                                            \
    H(OP_MOVE_RESULT),                                                            \
    H(OP_XOR_INT_LIT8),                                                            \
    H(OP_DOUBLE_TO_FLOAT),                                                            \
    H(OP_INVOKE_DIRECT_RANGE),                                                            \
    H(OP_USHR_LONG),                                                            \
    H(OP_MONITOR_EXIT),                                                            \
    H(OP_RETURN),                                                            \
    H(OP_IF_EQ),                                                            \
    H(OP_USHR_INT_2ADDR),                                                            \
    H(OP_INVOKE_DIRECT),                                                            \
    H(OP_UNUSED_79),                                                            \
    H(OP_UNUSED_7A),                                                            \
    H(OP_REM_FLOAT),                                                            \
    H(OP_IPUT_BYTE),                                                            \
    H(OP_IPUT_WIDE_QUICK),                                                            \
    H(OP_ADD_INT_LIT8),                                                            \
    H(OP_INVOKE_VIRTUAL_QUICK),                                                            \
    H(OP_MOVE_OBJECT_16),                                                            \
    H(OP_MUL_LONG_2ADDR),                                                            \
    H(OP_OR_INT),                                                            \
    H(OP_IPUT_SHORT_QUICK),                                                            \
    H(OP_OR_LONG_2ADDR),                                                            \
    H(OP_CONST_STRING_JUMBO),                                                            \
    H(OP_INVOKE_CUSTOM),                                                            \
    H(OP_REM_DOUBLE_2ADDR),                                                            \
    H(OP_OR_INT_LIT16),                                                            \
    H(OP_IF_NE),                                                            \
    H(OP_MOVE_OBJECT_FROM16),                                                            \
    H(OP_AGET_CHAR),                                                            \
    H(OP_CONST_4),                                                            \
    H(OP_SHL_LONG_2ADDR),                                                            \
    H(OP_IF_GTZ),                                                            \
    H(OP_SHL_INT_2ADDR),                                                            \
    H(OP_INT_TO_LONG),                                                            \
    H(OP_REM_LONG),                                                            \
    H(OP_AND_INT_LIT16),                                                            \
    H(OP_IGET_WIDE),                                                            \
    H(OP_REM_INT),                                                            \
    H(OP_ADD_FLOAT),                                                            \
    H(OP_REM_LONG_2ADDR),                                                            \
    H(OP_REM_INT_2ADDR),                                                            \
    H(OP_APUT_BOOLEAN),                                                            \
    H(OP_LONG_TO_INT),                                                            \
    H(OP_IGET_BOOLEAN),                                                            \
    H(OP_SUB_INT_2ADDR),                                                            \
    H(OP_ADD_LONG),                                                            \
    H(OP_SGET_OBJECT),                                                            \
    H(OP_IGET_BYTE),                                                            \
    H(OP_OR_INT_2ADDR),                                                            \
    H(OP_APUT_WIDE),                                                            \
    H(OP_INVOKE_INTERFACE),                                                            \
    H(OP_MUL_INT_2ADDR),                                                            \
    H(OP_DIV_DOUBLE),                                                            \
    H(OP_SUB_FLOAT_2ADDR),                                                            \
    H(OP_MUL_LONG),                                                            \
    H(OP_INVOKE_POLYMORPHIC_RANGE),                                                            \
    H(OP_INVOKE_SUPER),                                                            \
    H(OP_IF_EQZ),                                                            \
    H(OP_MOVE_OBJECT),                                                            \
    H(OP_DIV_DOUBLE_2ADDR),                                                            \
    H(OP_INVOKE_STATIC),                                                            \
    H(OP_APUT),                                                            \
    H(OP_SHR_LONG),                                                            \
    H(OP_CONST_WIDE_16),                                                            \
    H(OP_AND_INT),                                                            \
    H(OP_MOVE),                                                            \
    H(OP_SPUT_BYTE),                                                            \
    H(OP_INT_TO_FLOAT),                                                            \
    H(OP_SUB_DOUBLE),                                                            \
    H(OP_IF_LTZ),                                                            \
    H(OP_INVOKE_STATIC_RANGE),                                                            \
    H(OP_IGET_OBJECT_QUICK),                                                            \
    H(OP_DIV_FLOAT_2ADDR),                                                            \
    H(OP_GOTO_32),                                                            \
    H(OP_DIV_LONG),                                                            \
    H(OP_MUL_INT_LIT16),                                                            \
    H(OP_SGET_WIDE),                                                            \
    H(OP_DIV_FLOAT),                                                            \
    H(OP_USHR_INT_LIT8),                                                            \
    H(OP_SGET_BYTE),                                                            \
    H(OP_CONST_WIDE),                                                            \
    H(OP_OR_LONG),                                                            \
    H(OP_MONITOR_ENTER),                                                            \
    H(OP_AGET_WIDE),                                                            \
    H(OP_INVOKE_CUSTOM_RANGE),                                                            \
    H(OP_AGET),                                                            \
    H(OP_SHR_INT_LIT8),                                                            \
    H(OP_NEW_ARRAY),                                                            \
    H(OP_IPUT_BOOLEAN_QUICK),                                                            \
    H(OP_DOUBLE_TO_INT),                                                            \
    H(OP_RETURN_VOID),                                                            \
    H(OP_NEW_INSTANCE),                                                            \
    H(OP_INVOKE_VIRTUAL),                                                            \
    H(OP_DOUBLE_TO_LONG),                                                            \
    H(OP_SHL_INT_LIT8),                                                            \
    H(OP_DIV_INT_LIT8),                                                            \
    H(OP_CONST_STRING),                                                            \
    H(OP_ADD_LONG_2ADDR),                                                            \
    H(OP_INVOKE_POLYMORPHIC),                                                            \
    H(OP_AGET_BOOLEAN),                                                            \
    H(OP_IPUT_BOOLEAN),                                                            \
    H(OP_MUL_FLOAT),                                                            \
    H(OP_AND_LONG),                                                            \
    H(OP_IGET_CHAR),                                                            \
    H(OP_MUL_DOUBLE),                                                            \
    H(OP_DIV_LONG_2ADDR),                                                            \
    H(OP_SUB_FLOAT),                                                            \
    H(OP_IGET_BOOLEAN_QUICK),                                                            \
    H(OP_MUL_INT_LIT8),                                                            \
    H(OP_MOVE_WIDE_16),                                                            \
    H(OP_IPUT),                                                            \
    H(OP_SHR_INT),                                                            \
    H(OP_IPUT_SHORT),                                                            \
    H(OP_IF_LT),                                                            \
    H(OP_AND_INT_LIT8),                                                            \
    H(OP_IGET_WIDE_QUICK),                                                            \
    H(OP_FILLED_NEW_ARRAY),                                                            \
    H(OP_CONST),                                                            \
    H(OP_RSUB_INT),                                                            \
    H(OP_MOVE_WIDE_FROM16),                                                            \
    H(OP_APUT_BYTE),                                                            \
    H(OP_IGET),                                                            \
    H(OP_REM_FLOAT_2ADDR),                                                            \
    H(OP_SPUT_SHORT),                                                            \
    H(OP_PACKED_SWITCH),                                                            \
    H(OP_IGET_SHORT),                                                            \
    H(OP_XOR_INT_LIT16),                                                            \
    H(OP_AND_INT_2ADDR),                                                            \
    H(OP_FILL_ARRAY_DATA),                                                            \
    H(OP_SGET),                                                            \
    H(OP_NEG_LONG),                                                            \
    H(OP_INVOKE_VIRTUAL_QUICK_RANGE),                                                            \
    H(OP_UNUSED_F3),                                                            \
    H(OP_UNUSED_F4),                                                            \
    H(OP_UNUSED_F5),                                                            \
    H(OP_UNUSED_F6),                                                            \
    H(OP_UNUSED_F7),                                                            \
    H(OP_UNUSED_F8),                                                            \
    H(OP_UNUSED_F9),                                                            \
    H(OP_XOR_LONG_2ADDR),                                                            \
    H(OP_CONST_WIDE_32),                                                            \
    H(OP_SHR_INT_2ADDR),                                                            \
    H(OP_SPUT),                                                            \
    H(OP_RETURN_OBJECT),                                                            \
    H(OP_REM_INT_LIT8),                                                            \
};



#endif  // LIBDEX_DEXOPCODES_H_