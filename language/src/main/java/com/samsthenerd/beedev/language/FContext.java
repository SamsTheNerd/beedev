package com.samsthenerd.beedev.language;

import com.samsthenerd.beedev.language.sorts.FExpr;
import com.samsthenerd.beedev.language.sorts.FType;
import com.samsthenerd.beedev.utils.Hamt;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Optional;

public class FContext {
    // these bindings are meant for lazilly dealing with variables
//    private final Hamt<CombSym, FType> exprTypeBindings; // TODO: should prob attach type bindings to expressions instead? like as a cache sorta thing?
//    private final Hamt<CombSym, FExpr> exprBindings;
//
//    private final Hamt<CombSym, FType> typeDefs;
//
//    public FContext(){
//        exprTypeBindings = Hamt.empty();
//        exprBindings = Hamt.empty();
//        typeDefs = Hamt.empty();
//    }
//
//    public static FContext fromModules(Collection<FModule> modules){
//        FContext ctx = new FContext();
//        for(FModule module : modules){
//            for(Entry<CombSym, FExpr> exprBind : module.exprBindings().entrySet()){
//                ctx = ctx.bindExpr(exprBind.getKey(), exprBind.getValue());
//            }
//            for(Entry<CombSym, FType> typeDef : module.typeDefs().entrySet()){
//                ctx = ctx.defType(typeDef.getKey(), typeDef.getValue());
//            }
//        }
//        return ctx;
//    }
//
//    public FContext bindExprType(CombSym sym, FType type){
//        return new FContext(exprTypeBindings.assoc(sym, type), exprBindings, typeDefs);
//    }
//
//    public FContext bindExpr(CombSym sym, FExpr expr){
//        return new FContext(exprTypeBindings.assoc(sym, expr.getType(this)), exprBindings.assoc(sym, expr), typeDefs);
//    }
//
//    public FContext defType(CombSym sym, FType type){
//        return new FContext(exprTypeBindings, exprBindings, typeDefs);
//    }
//
//    private FContext(Hamt<CombSym, FType> newexprTypeBindings,
//                     Hamt<CombSym, FExpr> newVarTypes,
//                     Hamt<CombSym, FType> newTypeDefs) {
//        this.exprTypeBindings = newexprTypeBindings;
//        this.exprBindings = newVarTypes;
//        this.typeDefs = newTypeDefs;
//    }
//
//    public Optional<FType> getBoundType(CombSym sym){
//        return exprTypeBindings.get(sym);
//    }
}
