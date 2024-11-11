package com.samsthenerd.beedev.language.exprs;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.FContext;
import com.samsthenerd.beedev.language.sorts.FExpr;
import com.samsthenerd.beedev.language.sorts.FType;
import com.samsthenerd.beedev.language.types.FFuncType;
import com.samsthenerd.beedev.utils.TypeHelpers;
import com.samsthenerd.beedev.utils.TypeHelpers.Prenexed;

import java.util.Optional;

public record FApp(FExpr func, FExpr arg) implements FExpr {
//    public FType getType(FContext ctx) {
//        if (func.getType(ctx) instanceof FFuncType(FType fromT, FType toT)
//            && fromT.equals(arg.getType(ctx))
//        ) {
//            return toT;
//        }
//        return new FError("Could not apply arg: " + arg + " to expression: " + func);
//    }

    public FExpr substitute(FContext ctx, CombSym sym, FExpr withExpr) {
        return new com.samsthenerd.beedev.language.exprs.FApp(func.substitute(ctx, sym, withExpr), arg.substitute(ctx, sym, withExpr));
    }

//    @Override
//    public Optional<SynthRes> synthType(TypeEnv env) {
//        return func.synthType(env).map(res -> {
//            Prenexed pn = TypeHelpers.getPrenexedIdiot(res.type());
//            if(pn.instantiate() instanceof FFuncType(FType fromType, FType toType)
//                && arg.checkType(env, fromType)
//            ){
//                // TODO: need to like, instantiate in here somehow?
////                FType instT = TypeHelpers.getPrenexedIdiot(toType).instantiate();
//                // as in toType but with whatever quantifiers match between fromType and argType ?
////                return new SynthRes(instT, env.withExpr(this, instT));
//            }
//            return null;
//        });
//    }

    public boolean isValue() {
        return false;
    }

    public FExpr reduce(FContext ctx) {
        if (func instanceof FFunc ffunc) return ffunc.apply(ctx, arg);
        if (func.isValue()) return this;
        return new FApp(func.reduce(ctx), arg).reduce(ctx);
    }

//    public FExpr subType(FContext ctx, CombSym sym, FType withT) {
//        return new com.samsthenerd.beedev.language.exprs.FApp(func().subType(ctx, sym, withT), arg().subType(ctx, sym, withT));
//    }

    @Override
    public String sfgString() {
        return "ap(" + func() + "; " + arg() + ")";
    }

    public String toString(){ return sfgString(); }
}
