package com.samsthenerd.beedev.language.core.exprs;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.core.FContext;
import com.samsthenerd.beedev.language.core.FExpr;
import com.samsthenerd.beedev.language.core.FType;
import com.samsthenerd.beedev.language.core.types.FQuantType;

public record FTypeAbs(CombSym absSym, FExpr expr) implements FExpr {
    public FType getType(FContext ctx) {
        // TODO: need to check if type of expression is valid-ish here?
        // and also make it like, actually go into the type ? it might just give an error here rn
        return new FQuantType(absSym(), expr().getType(ctx));
    }

    public FExpr substitute(FContext ctx, CombSym sym, FExpr withExpr) {
        return new com.samsthenerd.beedev.language.core.exprs.FTypeAbs(absSym(), expr.substitute(ctx, sym, withExpr));
    }

    public boolean isValue() {
        return true;
    }

    public FExpr reduce(FContext ctx) {
        return this;
    }

    public FExpr subType(FContext ctx, CombSym sym, FType withT) {
        return new com.samsthenerd.beedev.language.core.exprs.FTypeAbs(absSym(), expr.subType(ctx, sym, withT));
    }

    @Override
    public String toString() {
        return "Lam[" + absSym() + "].(" + expr() + ")";
    }
}
