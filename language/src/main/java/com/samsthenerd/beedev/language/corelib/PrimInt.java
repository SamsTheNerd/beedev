package com.samsthenerd.beedev.language.corelib;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.core.FExpr.FPrimitive;
import com.samsthenerd.beedev.language.core.FType;
import com.samsthenerd.beedev.language.core.FType.FPrimType;

public record PrimInt(int num) implements FPrimitive {
    public static final FPrimType PRIM_INT_TYPE = new FPrimType(CombSym.of("core:Int"));

    @Override
    public FType getType() {
        return PRIM_INT_TYPE;
    }

    @Override
    public String toString(){
        return "Int[" + num + "]";
    }

    public static FFunc INT_ADD = FFunc.binaryPrimFunc(PRIM_INT_TYPE, PRIM_INT_TYPE, PRIM_INT_TYPE, (a,b) -> {
        // youch! performance bad here
        if (a instanceof PrimInt(int anum)
            && b instanceof PrimInt(int bnum)) {
            return new PrimInt(anum + bnum);
        }
        return new PrimInt(0); //this should not happen if the type checker is doing its job
    }
    );
}
