package com.samsthenerd.beedev.utils;

import com.samsthenerd.beedev.language.TcExpected;
import com.samsthenerd.beedev.language.TcExpected.TcCheck;
import com.samsthenerd.beedev.language.TcExpected.TcInfer;
import com.samsthenerd.beedev.language.TcMonad;
import com.samsthenerd.beedev.language.sorts.FExpr;
import com.samsthenerd.beedev.language.sorts.FType;
import com.samsthenerd.beedev.language.types.FMetaVar;
import com.samsthenerd.beedev.language.types.FQuantType;
import com.samsthenerd.beedev.language.types.FTypeVar;

import java.util.List;

// helper functions based on Arbitrary Rank HM system
public class TcHelpers {
    public static TcMonad<FType> inferPoly(FExpr expr){
        return null;
    }

    public static TcMonad<Unit> checkPoly(FExpr expr){
        return null;
    }

    // doesn't need to be monadic since we can just make new meta vars out of thin air
    public static FType instantiate(FType type){
        if(type instanceof FQuantType(List<FTypeVar> qVars, FType typeBody)){
            Hamt<FTypeVar, FType> subst = Hamt.empty();
            for(var v : qVars){
                subst = subst.assoc(v, FMetaVar.makeNew());
            }
            return typeBody.substitute(subst);
        }
        return type;
    }

    // instantiates a polymorphic type into the expected rho type (or checks it)
    public static TcMonad<Unit> instPoly(FType type, TcExpected expected){
        return switch(expected){
            case TcCheck(FType cType) -> null; //TODO: have this do a subs check -- also define how that'll work
            case TcInfer(FMetaVar mv) -> TcMonad.write(mv, instantiate(type));
        };
    }
}
