package com.samsthenerd.beedev.language.types;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.FContext;
import com.samsthenerd.beedev.language.sorts.FType;
import com.samsthenerd.beedev.utils.Hamt;

// used to indicate an error in type checking
public record FError(String eMsg) implements FType {
//    public boolean isRank(TypeEnv ctx, int rank) {
//        return true;
//    }

    public boolean isWellTyped(FContext ctx) {
        return false;
    }

    public FType substitute(Hamt<FTypeVar, FType> subst) {
        return this;
    }


    @Override
    public String sfgString() {
        return "[TypeError: " + eMsg + "]";
    }

//    @Override
//    public boolean isSubtypeOf(TypeEnv env, FType type) {
//        return false;
//    }
}
