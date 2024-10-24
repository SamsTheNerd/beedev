package com.samsthenerd.beedev.language.core;

import com.samsthenerd.beedev.language.CombSym;

public interface FExpr {

    // TODO: build error handling into this? or actually to a lot of these
    FType getType(FContext ctx);

    FExpr substitute(FContext ctx, CombSym sym, FExpr withExpr);

    FExpr subType(FContext ctx, CombSym sym, FType withT);

    boolean isValue();

    FExpr reduce(FContext ctx);

    // a string representation with more details
    default String debugString(){
        return toString();
    }

    // a string representation matching our systemf grammar
    String sfgString();
}
