package com.samsthenerd.beedev.language.core;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.utils.Hamt;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Optional;

public class FContext {
    // these bindings are meant for lazilly dealing with variables
    private final Hamt<CombSym, FType> exprTypeBindings; // TODO: should prob attach type bindings to expressions instead? like as a cache sorta thing?
    private final Hamt<CombSym, FExpr> exprBindings;

    private final Hamt<CombSym, FType> typeDefs;

    public FContext(){
        exprTypeBindings = Hamt.empty();
        exprBindings = Hamt.empty();
        typeDefs = Hamt.empty();
    }

    public static FContext fromModules(Collection<FModule> modules){
        FContext ctx = new FContext();
        for(FModule module : modules){
            for(Entry<CombSym, FExpr> exprBind : module.exprBindings().entrySet()){
                ctx = ctx.bindExpr(exprBind.getKey(), exprBind.getValue());
            }
            for(Entry<CombSym, FType> typeDef : module.typeDefs().entrySet()){
                ctx = ctx.defType(typeDef.getKey(), typeDef.getValue());
            }
        }
        return ctx;
    }

    public FContext bindExprType(CombSym sym, FType type){
        return new FContext(exprTypeBindings.assoc(sym, type), exprBindings, typeDefs);
    }

    public FContext bindExpr(CombSym sym, FExpr expr){
        return new FContext(exprTypeBindings.assoc(sym, expr.getType(this)), exprBindings.assoc(sym, expr), typeDefs);
    }

    public FContext defType(CombSym sym, FType type){
        return new FContext(exprTypeBindings, exprBindings, typeDefs);
    }

    private FContext(Hamt<CombSym, FType> newexprTypeBindings,
                     Hamt<CombSym, FExpr> newVarTypes,
                     Hamt<CombSym, FType> newTypeDefs) {
        this.exprTypeBindings = newexprTypeBindings;
        this.exprBindings = newVarTypes;
        this.typeDefs = newTypeDefs;
    }

    public Optional<FType> getBoundType(CombSym sym){
        return exprTypeBindings.get(sym);
    }
//
//    // determine if a type is valid in this context.
//    public boolean isType(FType type){
//        switch (type){
//            // i don't think this is sufficient. it prob needs to check if the bound type is valid as well. would that recurse though?
//            case FTypeVar v -> {return exprTypeBindings.get(v.sym()).isPresent();}
//            case FFuncType ft -> {return isType(ft.fromType()) && isType(ft.toType()); }
//            case FQuantType qt -> {return this.bindType(qt.typeVar().sym(), qt.typeVar()).isType(qt.typeBody()); }
//            default -> {return false;}
//        }
//    }
//
//    // no inference rn ig
//    public Optional<FType> getType(FExpression expr){
//        switch (expr){
//            case FVar v -> {
//                return exprTypeBindings.get(v.sym());
//            }
//            case FLambda(FType argTp, FVar arg, FExpression body) -> {
//                if(isType(argTp)) return Optional.empty();
//                return this.bindType(arg.sym(), argTp).getType(body)
//                    .map(fromTp -> new FFuncType(argTp, fromTp));
//            }
//            case FApp(FExpression f, FExpression arg) -> {
//                if(getType(f).orElse(null) instanceof FFuncType(var fT, var tT)){
//                    return getType(arg).map(argType -> tT);
//                }
//                return Optional.empty();
//            }
//            case FTypeAbs(FTypeVar typeVar, FExpression body) -> {
//                return bindType(typeVar.sym(), typeVar).getType(body).map(bodyT -> new FQuantType(typeVar, bodyT));
//            }
//            case FTypeApp(FType actualT, FExpression vagueF) -> {
//                if(getType(vagueF).orElse(null) instanceof FQuantType(FTypeVar tV, FType bodyT)
//                && isType(actualT)){
//                    return bodyT.substitute(actualT, tV);
//                }
//            }
//            default -> { return Optional.empty(); }
//        }
//        return Optional.empty();
//    }
}
