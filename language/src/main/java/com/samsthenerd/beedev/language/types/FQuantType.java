package com.samsthenerd.beedev.language.types;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.sorts.FType;
import com.samsthenerd.beedev.utils.Hamt;

import java.util.List;

// TODO: should this be like, [FTypeVar] rather than CombSym ?
public record FQuantType(List<FTypeVar> qVars, FType typeBody) implements FType {
//    public boolean isRank(TypeEnv env, int rank) {
//        return true;
//    }

    public FType substitute(Hamt<FTypeVar, FType> subst) {
        // assume alpha renaming prevents capturing here?
        return new FQuantType(qVars, typeBody.substitute(subst.dissocAll(qVars)));
    }

    public String debugString() {
        return "all[" + qVars() + "." + typeBody + "]";
    }

    public String sfgString(){
        return "all[" + qVars() + ";" +  typeBody + "]";
    }

//    @Override
//    public boolean isSubtypeOf(TypeEnv env, FType type) {
//        return true;
//    }

    @Override
    public String toString() {
        return sfgString();
    }
}
