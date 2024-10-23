package com.samsthenerd.beedev.language.core.types;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.core.FContext;
import com.samsthenerd.beedev.language.core.FType;

// used to indicate an error in type checking
public record FError(String eMsg) implements FType {
    public boolean isRank(FContext ctx, int rank) {
        return true;
    }

    public boolean isWellTyped(FContext ctx) {
        return false;
    }

    public FType substitute(FContext ctx, CombSym sym, FType withT) {
        return this;
    }
}
