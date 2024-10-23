package com.samsthenerd.beedev.language.core.types;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.core.FContext;
import com.samsthenerd.beedev.language.core.FType;

public record FTypeVar(CombSym sym) implements FType {
    public boolean isRank(FContext ctx, int k) {
        // mm depends on ??
        return true; // idk
    }

    public boolean isWellTyped(FContext ctx) {
        return ctx.getBoundType(sym()).map(t -> t.isWellTyped(ctx)).orElse(false);
    }

    public FType substitute(FContext ctx, CombSym symS, FType withT) {
        return symS.equals(sym()) ? withT : this;
    }

    public String toString() {
        return "T[" + sym.toString() + "]";
    }
}
