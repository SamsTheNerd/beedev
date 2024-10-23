package com.samsthenerd.beedev.language.core.exprs;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.core.FContext;
import com.samsthenerd.beedev.language.core.FExpr;
import com.samsthenerd.beedev.language.core.FType;

// used to add primitive-ish types. This likely won't be the preferred way to add new types.
public interface FPrimitive extends FExpr {
    FType getType();

    default FType getType(FContext ctx) {
        return getType();
    }

    default FExpr substitute(FContext ctx, CombSym sym, FExpr withExpr) {
        return this;
    }

    default boolean isValue() {
        return true;
    }

    default FExpr reduce(FContext ctx) {
        return this;
    }

    default FExpr subType(FContext ctx, CombSym sym, FType withT) {
        return this;
    }
}
