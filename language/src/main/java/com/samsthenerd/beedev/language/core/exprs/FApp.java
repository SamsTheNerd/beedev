package com.samsthenerd.beedev.language.core.exprs;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.core.FContext;
import com.samsthenerd.beedev.language.core.FExpr;
import com.samsthenerd.beedev.language.core.FType;
import com.samsthenerd.beedev.language.core.types.FError;
import com.samsthenerd.beedev.language.core.types.FFuncType;

public record FApp(FExpr func, FExpr arg) implements FExpr {
    public FType getType(FContext ctx) {
        if (func.getType(ctx) instanceof FFuncType(FType fromT, FType toT)
            && fromT.equals(arg.getType(ctx))
        ) {
            return toT;
        }
        return new FError("Could not apply arg: " + arg + " to expression: " + func);
    }

    public FExpr substitute(FContext ctx, CombSym sym, FExpr withExpr) {
        return new com.samsthenerd.beedev.language.core.exprs.FApp(func.substitute(ctx, sym, withExpr), arg.substitute(ctx, sym, withExpr));
    }

    public boolean isValue() {
        return false;
    }

    public FExpr reduce(FContext ctx) {
        if (func instanceof FFunc ffunc) return ffunc.apply(ctx, arg);
        if (func.isValue()) return this;
        return new com.samsthenerd.beedev.language.core.exprs.FApp(func.reduce(ctx), arg).reduce(ctx);
    }

    public FExpr subType(FContext ctx, CombSym sym, FType withT) {
        return new com.samsthenerd.beedev.language.core.exprs.FApp(func().subType(ctx, sym, withT), arg().subType(ctx, sym, withT));
    }

    @Override
    public String toString() {
        return "app(" + func() + "; " + arg() + ")";
    }
}
