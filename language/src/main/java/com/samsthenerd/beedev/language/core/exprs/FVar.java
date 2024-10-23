package com.samsthenerd.beedev.language.core.exprs;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.core.FContext;
import com.samsthenerd.beedev.language.core.FExpr;
import com.samsthenerd.beedev.language.core.FType;

// does this need a type attached to it?
public record FVar(CombSym sym, FType type) implements FExpr {
    public FType getType(FContext ctx) {
        return type;
    }

    public FExpr substitute(FContext ctx, CombSym subSym, FExpr withExpr) {
        if (sym().equals(subSym)) return withExpr;
        return this;
    }

    public boolean isValue() {
        return false;
    }

    public FExpr reduce(FContext ctx) {
        return this;
    }

    public FExpr subType(FContext ctx, CombSym subSym, FType withT) {
        return new com.samsthenerd.beedev.language.core.exprs.FVar(sym, type().substitute(ctx, subSym, withT));
    }
}
