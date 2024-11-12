package com.samsthenerd.beedev.language.exprs;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.FContext;
import com.samsthenerd.beedev.language.TcExpected;
import com.samsthenerd.beedev.language.TcMonad;
import com.samsthenerd.beedev.language.sorts.FExpr;
import com.samsthenerd.beedev.language.sorts.FType;
import com.samsthenerd.beedev.language.types.FFuncType;
import com.samsthenerd.beedev.utils.TcHelpers;
import com.samsthenerd.beedev.utils.TypeHelpers;
import com.samsthenerd.beedev.utils.Unit;

import java.util.Optional;

public record FApp(FExpr func, FExpr arg) implements FExpr {
//    public FExpr substitute(FContext ctx, CombSym sym, FExpr withExpr) {
//        return new com.samsthenerd.beedev.language.exprs.FApp(func.substitute(ctx, sym, withExpr), arg.substitute(ctx, sym, withExpr));
//    }


    @Override
    public TcMonad<Unit> typeCheck(TcExpected expected) {
        return func.synthType()
            .bind(TcMonad::unifyFun)
            .bind(ftype ->
                TcHelpers.checkPoly(arg, ftype.fromType())
                    .seq(TcHelpers.instPoly(ftype.toType(), expected)));
    }

    public boolean isValue() {
        return false;
    }

//    public FExpr reduce(FContext ctx) {
//        if (func instanceof FFunc ffunc) return ffunc.apply(ctx, arg);
//        if (func.isValue()) return this;
//        return new FApp(func.reduce(ctx), arg).reduce(ctx);
//    }

//    public FExpr subType(FContext ctx, CombSym sym, FType withT) {
//        return new com.samsthenerd.beedev.language.exprs.FApp(func().subType(ctx, sym, withT), arg().subType(ctx, sym, withT));
//    }

    @Override
    public String sfgString() {
        return "(" + func().sfgString() + " " + arg().sfgString() + ")";
    }

    public String toString(){ return sfgString(); }
}
