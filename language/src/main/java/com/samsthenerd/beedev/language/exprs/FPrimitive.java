package com.samsthenerd.beedev.language.exprs;

import com.samsthenerd.beedev.language.*;
import com.samsthenerd.beedev.language.TcExpected.TcCheck;
import com.samsthenerd.beedev.language.TcExpected.TcInfer;
import com.samsthenerd.beedev.language.sorts.FExpr;
import com.samsthenerd.beedev.language.sorts.FType;
import com.samsthenerd.beedev.language.types.FMetaVar;
import com.samsthenerd.beedev.utils.Unit;

// used to add primitive-ish types. This likely won't be the preferred way to add new types.
public interface FPrimitive extends FExpr {
    FType getType();

//    default FType getType(FContext ctx) {
//        return getType();
//    }
//
//    default FExpr substitute(FContext ctx, CombSym sym, FExpr withExpr) {
//        return this;
//    }

    default boolean isValue() {
        return true;
    }

    default FExpr reduce(FContext ctx) {
        return this;
    }

//    default FExpr subType(FContext ctx, CombSym sym, FType withT) {
//        return this;
//    }

//    default TcMonad<Unit> typeCheck(TcMonad<Unit> env, TcExpected expected){
//        return switch(expected){
//            case TcInfer(FMetaVar meta) -> env.write(meta, getType());
//            case TcCheck(FType type) -> null; // TODO: figure out inference sorta thing here ?
//        };
//    }
}
