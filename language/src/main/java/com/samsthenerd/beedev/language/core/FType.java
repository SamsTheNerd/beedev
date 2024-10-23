package com.samsthenerd.beedev.language.core;

import com.samsthenerd.beedev.language.CombSym;

public interface FType {

    boolean isRank(FContext ctx, int k);

    boolean isWellTyped(FContext ctx);

    FType substitute(FContext ctx, CombSym sym, FType withT);

}
