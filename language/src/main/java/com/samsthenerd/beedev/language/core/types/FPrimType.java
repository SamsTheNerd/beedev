package com.samsthenerd.beedev.language.core.types;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.core.FContext;
import com.samsthenerd.beedev.language.core.FType;

// sure?
public record FPrimType(CombSym name) implements FType {
    // force prims into being rank 0 ?
    public boolean isRank(FContext ctx, int k) {
        return true;
    }

    public boolean isWellTyped(FContext ctx) {
        return true; // TODO: maybe figure out like, a module sorta system that can enable/disable prims
    }

    public FType substitute(FContext ctx, CombSym sym, FType withT) {
        return this;
    }

    @Override
    public String sfgString() {
        return name().getSpecifier(); // probably fine? idk, need to figure out how to parse these anyhow
    }

    @Override
    public String debugString() {
        return "Prim[" + name + "]";
    }

    public String toString(){ return sfgString();}
}
