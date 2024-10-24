package com.samsthenerd.beedev.language.core.types;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.core.FContext;
import com.samsthenerd.beedev.language.core.FType;

public record FQuantType(CombSym faSym, FType typeBody) implements FType {
    public boolean isRank(FContext ctx, int rank) {
        return true;
    }

    public boolean isWellTyped(FContext ctx) {
        return typeBody().isWellTyped(ctx); // this doesn't seem,, complete?
    }

    public FType substitute(FContext ctx, CombSym sym, FType withT) {
        // assume alpha renaming prevents capturing here?
        // also TODO: rank restriction here ?
        return new com.samsthenerd.beedev.language.core.types.FQuantType(sym, withT.substitute(ctx, sym, withT));
    }



    public String debugString() {
        return "all[" + faSym() + "." + typeBody + "]";
    }

    public String sfgString(){
        return "all[" + faSym() + ";" +  typeBody + "]";
    }

    @Override
    public String toString() {
        return sfgString();
    }
}
