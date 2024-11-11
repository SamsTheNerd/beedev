package com.samsthenerd.beedev.language.sorts;

import com.samsthenerd.beedev.language.*;
import com.samsthenerd.beedev.language.TcExpected.TcCheck;
import com.samsthenerd.beedev.language.TcExpected.TcInfer;
import com.samsthenerd.beedev.language.types.FMetaVar;
import com.samsthenerd.beedev.utils.Unit;

import java.util.Optional;

public interface FExpr {

//     TODO: build error handling into this? or actually to a lot of these
//    FType getType(FContext ctx);

//    FExpr substitute(FContext ctx, CombSym sym, FExpr withExpr);

//    FExpr subType(FContext ctx, CombSym sym, FType withT);

    // effectively unifying
    default TcMonad<Unit> checkType(FType type){
        return typeCheck(new TcCheck(type));
    }

    // takes a best guess at the type of this expression and also gives a new context
    default TcMonad<FType> synthType(){
        FMetaVar newVar = FMetaVar.makeNew();
        return typeCheck(new TcInfer(newVar)).seq(
            TcMonad.read(newVar).fmap(opt -> opt.orElse(newVar))
        );
    }

    // we assume everything here is a monotype.
    TcMonad<Unit> typeCheck(TcExpected expected);

    boolean isValue();

//    FExpr reduce(FContext ctx);

    // a string representation with more details
    default String debugString(){
        return toString();
    }

    // a string representation matching our systemf grammar
    String sfgString();
}
