package com.samsthenerd.beedev.language.core;

import com.samsthenerd.beedev.language.CombSym;

public interface FType {

    boolean isRank(FContext ctx, int k);

    boolean isWellTyped(FContext ctx);

    FType substitute(FContext ctx, CombSym sym, FType withT);

    // a string representation with more details
    default String debugString(){
        return toString();
    }

    // a string representation matching our systemf grammar
    String sfgString();

}
