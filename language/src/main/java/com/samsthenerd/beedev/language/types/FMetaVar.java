package com.samsthenerd.beedev.language.types;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.sorts.FType;
import com.samsthenerd.beedev.utils.Hamt;

import java.util.UUID;

public record FMetaVar(UUID id) implements FType {
    public static FMetaVar makeNew(){
        return new FMetaVar(UUID.randomUUID());
    }

//    @Override
//    public boolean isRank(TypeEnv env, int k) {
//        return false;
//    }

    @Override
    public FType substitute(Hamt<FTypeVar, FType> subst) {
        return this;
    }

    @Override
    public String sfgString() {
        return id.toString();
    }

//    @Override
//    public boolean isSubtypeOf(TypeEnv env, FType type) {
//        return true;
//    }
}
