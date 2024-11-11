package com.samsthenerd.beedev.language.types;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.sorts.FType;
import com.samsthenerd.beedev.utils.Hamt;

public record FFuncType(FType fromType, FType toType) implements FType {
//    public boolean isRank(TypeEnv env, int k) {
//        if (k == 0) return fromType.isRank(env, 0) && fromType.isRank(env, 0);
//        return fromType.isRank(env, k - 1) && toType.isRank(env, k);
//    }

//    public boolean isWellTyped(FContext ctx) {
//        return fromType().isWellTyped(ctx) && toType().isWellTyped(ctx);
//    }

    public FType substitute(Hamt<FTypeVar, FType> subst) {
        return new FFuncType(fromType().substitute(subst), toType().substitute(subst));
    }

    @Override
    public String sfgString() {
        return "(" + fromType().sfgString() + " -> " + toType().sfgString() + ")";
    }

//    @Override
//    public boolean isSubtypeOf(TypeEnv env, FType type) {
//        return true;
//    }

    public String toString(){
        return sfgString();
    }
}
