package com.samsthenerd.beedev.language.exprs;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.FContext;
import com.samsthenerd.beedev.language.TcExpected;
import com.samsthenerd.beedev.language.TcExpected.TcCheck;
import com.samsthenerd.beedev.language.TcExpected.TcInfer;
import com.samsthenerd.beedev.language.TcMonad;
import com.samsthenerd.beedev.language.sorts.FExpr;
import com.samsthenerd.beedev.language.sorts.FType;
import com.samsthenerd.beedev.language.types.FMetaVar;
import com.samsthenerd.beedev.utils.TcHelpers;
import com.samsthenerd.beedev.utils.Unit;

// an expression variable that will either be filled later or is referencing some imported expression with the same symbol
// it is expected that sym is *the full symbol* for whatever it's referencing
public record FVar(CombSym sym) implements FExpr {
    public FExpr substitute(FContext ctx, CombSym subSym, FExpr withExpr) {
        if (sym().equals(subSym)) return withExpr;
        return this;
    }

    @Override
    public TcMonad<Unit> typeCheck(TcExpected expected) {
        TcMonad<FType> lvm = TcMonad.lookupVarStrict(this);
        return lvm.bind(type -> TcHelpers.instPoly(type, expected));
    }

    public boolean isValue() {
        return false;
    }

    public FExpr reduce(FContext ctx) {
        return this;
    }

    public String debugString(){
        return sym.asString();
    }

    public String sfgString(){
        return sym.asString();
    }

    public String toString(){
        return sfgString();
    }
}
