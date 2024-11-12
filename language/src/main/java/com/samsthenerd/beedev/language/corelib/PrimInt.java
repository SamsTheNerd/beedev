package com.samsthenerd.beedev.language.corelib;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.FContext;
import com.samsthenerd.beedev.language.exprs.FPrimitive;
import com.samsthenerd.beedev.language.FModule;
import com.samsthenerd.beedev.language.sorts.FExpr;
import com.samsthenerd.beedev.language.sorts.FType;
import com.samsthenerd.beedev.language.types.FPrimType;
import com.samsthenerd.beedev.language.exprs.FFunc;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;

public record PrimInt(int num) implements FPrimitive {
    public static final String NAMESPACE = "core.prims.int";
    public static final FPrimType PRIM_INT_TYPE = new FPrimType(CombSym.of(NAMESPACE, "Int"));

    @Override
    public FType getType() {
        return PRIM_INT_TYPE;
    }

//    @Override
//    public FExpr substitute(FContext ctx, CombSym sym, FExpr withExpr) {
//        return null;
//    }

    @Override
    public String debugString(){
        return "Int[" + num + "]";
    }

    public String sfgString(){
        return Integer.toString(num);
    }

    public String toString(){ return sfgString(); }

    public static FFunc INT_ADD = FFunc.binaryPrimFunc(PRIM_INT_TYPE, PRIM_INT_TYPE, PRIM_INT_TYPE, (a, b) -> {
        // youch! performance bad here
        if (a instanceof PrimInt(int anum)
            && b instanceof PrimInt(int bnum)) {
            return new PrimInt(anum + bnum);
        }
        return new PrimInt(0); //this should not happen if the type checker is doing its job
    }, CombSym.of(NAMESPACE, "intadd")
    );

    // TODO: make better way of constructing modules?
    public static final FModule INT_MODULE = new FModule(
        CombSym.of(NAMESPACE),
        Map.ofEntries(
            new SimpleEntry<>(CombSym.of(NAMESPACE, "intadd"), INT_ADD)
        ),
        Map.ofEntries(
            new SimpleEntry<>(CombSym.of(NAMESPACE, "Int"), PRIM_INT_TYPE)
        ),
        List.of()
    );
}
