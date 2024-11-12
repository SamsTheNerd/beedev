package com.samsthenerd.beedev.utils;

import com.samsthenerd.beedev.language.TcExpected;
import com.samsthenerd.beedev.language.TcExpected.TcCheck;
import com.samsthenerd.beedev.language.TcExpected.TcInfer;
import com.samsthenerd.beedev.language.TcMonad;
import com.samsthenerd.beedev.language.sorts.FExpr;
import com.samsthenerd.beedev.language.sorts.FType;
import com.samsthenerd.beedev.language.types.FFuncType;
import com.samsthenerd.beedev.language.types.FMetaVar;
import com.samsthenerd.beedev.language.types.FQuantType;
import com.samsthenerd.beedev.language.types.FTypeVar;

import java.util.*;

// helper functions based on Arbitrary Rank HM system
public class TcHelpers {
    public static TcMonad<FType> inferPoly(FExpr expr){
        return expr.synthType().bind(exprTyMono ->
            TcMonad.getEnvTypes().bind(envTys ->
            TcMonad.getMetaVars(envTys).bind(envMvs ->
            TcMonad.getMetaVars(List.of(exprTyMono)).bind(exprMvs -> {
                var exprMvsFresh = new ArrayList<>(exprMvs);
                exprMvsFresh.removeAll(envMvs);
                return quantify(exprMvsFresh, exprTyMono);
                }
                ))));
    }

    public static TcMonad<Unit> checkPoly(FExpr expr, FType type){
//        return TcMonad.unit();
        return expr.checkType(type); // TODO: handle actual polymorphism
    }

    // checks if t1 can be used in place of t2 -- going based off of paper rn but this will probably be where we modify things to make them cool.
    public static TcMonad<Unit> subsCheck(FType t1, FType t2){
        if(t1 instanceof FQuantType(List<FTypeVar> qVars, FType typeBody)){
            return subsCheck(instantiate(t1), t2);
        }
        if(t2 instanceof FFuncType(FType frT2, FType toT2)){
            return TcMonad.unifyFun(t1).bind(ftype1 -> subsCheck(frT2, ftype1.fromType()).seq(subsCheck(ftype1.toType(), toT2)));
        }
        if(t1 instanceof FFuncType(FType frT1, FType toT1)){
            return TcMonad.unifyFun(t2).bind(ftype2 -> subsCheck(ftype2.fromType(), frT1).seq(subsCheck(toT1, ftype2.toType())));
        }
        return TcMonad.unify(t1, t2);
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
            case TcCheck(FType cType) -> subsCheck(cType, type);
            case TcInfer(FMetaVar mv) -> TcMonad.write(mv, instantiate(type));
        };
    }

    public static TcMonad<FType> quantify(Collection<FMetaVar> qVars, FType type){
        if(qVars.isEmpty()) return TcMonad.of(type);
        TcMonad<Unit> qm = TcMonad.unit();
        List<FTypeVar> newBinders = new ArrayList<>();
        for(var qv : qVars){
            FTypeVar tv = FTypeVar.makeNew();
            qm = qm.seq(TcMonad.write(qv, tv));
            newBinders.add(tv);
        }
        return qm.seq(TcMonad.zonkType(type).fmap(zTy -> new FQuantType(newBinders, zTy)));
    }

//    public static TcMonad<FType> canonicalize(FType type){
//
//        TcMonad.getFreeTypeVars(List.of(type)).bind(list -> {
//            int num = 0;
//            for(var )
//        })
//    }
}
