package com.samsthenerd.beedev.language.core.types;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.core.FContext;
import com.samsthenerd.beedev.language.core.FType;

public record FFuncType(FType fromType, FType toType) implements FType {
    public boolean isRank(FContext ctx, int k) {
        if (k == 0) return fromType.isRank(ctx, 0) && fromType.isRank(ctx, 0);
        return fromType.isRank(ctx, k - 1) && toType.isRank(ctx, k);
    }

    public boolean isWellTyped(FContext ctx) {
        return fromType().isWellTyped(ctx) && toType().isWellTyped(ctx);
    }

    public FType substitute(FContext ctx, CombSym sym, FType withT) {
        return new com.samsthenerd.beedev.language.core.types.FFuncType(fromType().substitute(ctx, sym, withT), toType().substitute(ctx, sym, withT));
    }


    @Override
    public String sfgString() {
        return "(" + fromType().sfgString() + " -> " + toType().sfgString() + ")";
    }

    public String toString(){
        return sfgString();
    }
}
