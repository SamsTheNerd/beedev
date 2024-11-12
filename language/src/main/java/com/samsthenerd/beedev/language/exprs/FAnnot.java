package com.samsthenerd.beedev.language.exprs;

import com.samsthenerd.beedev.language.*;
import com.samsthenerd.beedev.language.TcExpected.TcCheck;
import com.samsthenerd.beedev.language.TcExpected.TcInfer;
import com.samsthenerd.beedev.language.sorts.FExpr;
import com.samsthenerd.beedev.language.sorts.FType;
import com.samsthenerd.beedev.language.types.FMetaVar;
import com.samsthenerd.beedev.utils.Unit;

// some expression with a type annotation attached to it
public record FAnnot(FExpr expr, FType type) implements FExpr{

//    @Override
//    public FExpr substitute(FContext ctx, CombSym sym, FExpr withExpr) {
//        return expr.substitute(ctx, sym, withExpr);
//    }

//    @Override
//    public TcMonad<FType> synthType(TcMonad<Unit> env) {
//        // TODO: alpha rename the type if needed?
//    }

    @Override
    public boolean isValue() {
        return expr.isValue();
    }

//    @Override
//    public FExpr reduce(FContext ctx) {
//        return expr.reduce(ctx);
//    }

    @Override
    public TcMonad<Unit> typeCheck(TcExpected expected){
//        return switch (expected) {
//            case TcInfer(FMetaVar meta) -> tc.write(meta, type); // TODO: need to instantiate here? or somewhere ?
//            case TcCheck(FType checkType) -> expr.typeCheck(tc, expected); // TODO: should this have a way to avoid deep eval-ing?
//        };
        return TcMonad.unit();
    }

    @Override
    public String sfgString() {
        return expr.sfgString() + " :: " + type.sfgString();
    }

    @Override
    public String toString() {
        return sfgString();
    }
}
