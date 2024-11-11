package com.samsthenerd.beedev.language.types;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.sorts.FType;
import com.samsthenerd.beedev.utils.Hamt;

public record FTypeVar(CombSym sym, int rank) implements FType {

    public FTypeVar(CombSym sym){
        this(sym, 0);
    }

//    public boolean isRank(TypeEnv env, int k) {
//        // mm depends on ??
//        return rank <= k;
//    }

    public FType substitute(Hamt<FTypeVar, FType> subst) {
        return subst.get(this).orElse(this);
    }

    public String debugString() {
        return "T[" + sym.toString() + "]";
    }

    public String sfgString(){ return sym.toString();}

    public String toString(){ return sfgString(); }

//    @Override
//    public boolean isSubtypeOf(TypeEnv env, FType type) {
//        return type.isRank(env, rank+1); //
//    }
}
