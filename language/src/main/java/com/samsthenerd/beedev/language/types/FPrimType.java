package com.samsthenerd.beedev.language.types;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.sorts.FType;
import com.samsthenerd.beedev.utils.Hamt;

// sure?
public record FPrimType(CombSym name) implements FType {
    // force prims into being rank 0 ?
//    public boolean isRank(TypeEnv ctx, int k) {
//        return true;
//    }

//    public boolean isWellTyped(FContext ctx) {
//        return true; // TODO: maybe figure out like, a module sorta system that can enable/disable prims
//    }

    public FType substitute(Hamt<FTypeVar, FType> subst) {
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

//    @Override
//    public boolean isSubtypeOf(TypeEnv env, FType type) {
//        // TODO: idk,,,
//        return true;
//    }
}
