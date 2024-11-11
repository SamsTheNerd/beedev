package com.samsthenerd.beedev.language.sorts;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.TcMonad;
import com.samsthenerd.beedev.language.types.FTypeVar;
import com.samsthenerd.beedev.utils.Hamt;
import com.samsthenerd.beedev.utils.Unit;

public interface FType {

//    boolean isRank(TypeEnv env, int k);

    // just for convenience of making stuff more readable
//    default boolean isMono(TypeEnv env){
//        return isRank(env, 0);
//    }

    // rho ::== monomorphic or function type (poly -> poly)
//    default boolean isRho(TypeEnv env){
//        return isRank(env, 0);
//    }

//    boolean isWellTyped(FContext ctx);

    FType substitute(Hamt<FTypeVar, FType> subst);

    // a string representation with more details
    default String debugString(){
        return toString();
    }

    // a string representation matching our systemf grammar
    String sfgString();

    // returns if this is a subtype of the argument.
//    boolean isSubtypeOf(TypeEnv env, FType type);
}
