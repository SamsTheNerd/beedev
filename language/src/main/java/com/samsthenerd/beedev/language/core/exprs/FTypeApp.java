package com.samsthenerd.beedev.language.core.exprs;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.core.FContext;
import com.samsthenerd.beedev.language.core.FExpr;
import com.samsthenerd.beedev.language.core.FType;
import com.samsthenerd.beedev.language.core.types.FError;
import com.samsthenerd.beedev.language.core.types.FQuantType;

public record FTypeApp(FType type, FExpr expr) implements FExpr {
    public FType getType(FContext ctx) {
        if (expr.getType(ctx) instanceof FQuantType(CombSym faSym, FType typeBody)
            && type().isWellTyped(ctx)
        ) {
            return typeBody.substitute(ctx, faSym, type);
        }
        return new FError("Could not apply type arg: " + type() + " in expression: " + expr());
    }

    public FExpr substitute(FContext ctx, CombSym sym, FExpr withExpr) {
        return new com.samsthenerd.beedev.language.core.exprs.FTypeApp(type(), expr.substitute(ctx, sym, withExpr));
    }

    public FExpr subType(FContext ctx, CombSym sym, FType withT) {
        return new com.samsthenerd.beedev.language.core.exprs.FTypeApp(type().substitute(ctx, sym, withT), expr.subType(ctx, sym, withT));
    }

    public boolean isValue() {
        return false;
    }

    public FExpr reduce(FContext ctx) {
        if (expr instanceof FTypeAbs(CombSym absSym, FExpr body)) {
            return body.subType(ctx, absSym, type());
        }
        if (expr.isValue()) return this;
        return new com.samsthenerd.beedev.language.core.exprs.FTypeApp(type(), expr().reduce(ctx)).reduce(ctx);
    }

    @Override
    public String sfgString() {
        return "App[" + type().sfgString() + "](" + expr().sfgString() + ")";
    }

    public String toString(){ return sfgString(); }
}
