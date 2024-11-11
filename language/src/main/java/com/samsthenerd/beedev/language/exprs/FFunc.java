package com.samsthenerd.beedev.language.exprs;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.FContext;
import com.samsthenerd.beedev.language.sorts.FExpr;
import com.samsthenerd.beedev.language.sorts.FType;
import com.samsthenerd.beedev.language.types.FFuncType;
import com.samsthenerd.beedev.language.types.FTypeVar;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface FFunc extends FExpr {

//    FType getArgType();

    // applies the function with the given argument
//    FExpr apply(FContext ctx, FExpr arg);

    default boolean isValue() {
        return true;
    }

//    @Override
//    default FExpr reduce(FContext ctx) {
//        return this;
//    }

    record FLambda(FVar arg,
                   FExpr expr) implements FFunc {
//        public FType getType(FContext ctx) {
//            return new FFuncType(argType(), expr.getType(ctx));
//        }
//
//        public FExpr substitute(FContext ctx, CombSym sym, FExpr withExpr) {
//            return sym.equals(argSym()) ? this :
//                new FLambda(sym, expr.substitute(ctx, sym, withExpr));
//        }
//
//        public FExpr subType(FContext ctx, CombSym sym, FType withT) {
//            return new FLambda(argType().substitute(ctx, sym, withT), argSym(), expr().subType(ctx, sym, withT));
//        }

//        @Override
//        public Optional<SynthRes> synthType(TypeEnv env) {
//            FType t = new FTypeVar(CombSym.arbitrary());
//            Optional<SynthRes> resOpt = expr.synthType(env.with(argSym, t));
//            if(resOpt.isPresent() && resOpt.get().type().isRho(env)){
//                FType fType = new FFuncType(t, resOpt.get().type());
//                return Optional.of(new SynthRes(fType, env));
//            }
//            return Optional.empty();
//        }

        // TODO: need like, skolo somethin here?

        public FExpr apply(FContext ctx, FExpr arg) {
            return expr().substitute(ctx, argSym, arg);
        }

        @Override
        public String sfgString(){
            return "lam{" + argSym.asString() + " -> " + expr.sfgString() + "}";
        }
    }

    record FPrimFunc(FType annot,
                     Function<FExpr, FExpr> f, CombSym id) implements FFunc {
//        @Override
//        public FType getType(FContext ctx) {
//            return new FFuncType(fromT(), toT());
//        }

        @Override
        public FExpr apply(FContext ctx, FExpr arg) {
            return f.apply(arg);
        }

        public String sfgString(){
            return id.toString();
        }

        @Override
        public String debugString() {
            return "primFunc[" + annot + "]@" + hashCode();
        }

        public FExpr substitute(FContext ctx, CombSym sym, FExpr withExpr) {
            return this;
        }

//        @Override
//        public Optional<SynthRes> synthType(TypeEnv env) {
//            return Optional.of(new SynthRes(annot, env));
//        }
    }

    static FFunc binaryPrimFunc(FType t1, FType t2, FType resT, BiFunction<FExpr, FExpr, FExpr> func, CombSym id) {
        return new FPrimFunc(new FFuncType(t1, new FFuncType(t2, resT)), (a) -> new FPrimFunc(new FFuncType(t2, resT), (b) -> func.apply(a, b), CombSym.of(id.asString() + "@" + UUID.randomUUID())), id);
    }
}
