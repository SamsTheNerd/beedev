package com.samsthenerd.beedev.language.exprs;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.FContext;
import com.samsthenerd.beedev.language.TcExpected;
import com.samsthenerd.beedev.language.TcExpected.TcCheck;
import com.samsthenerd.beedev.language.TcExpected.TcInfer;
import com.samsthenerd.beedev.language.TcMonad;
import com.samsthenerd.beedev.language.sorts.FExpr;
import com.samsthenerd.beedev.language.sorts.FType;
import com.samsthenerd.beedev.language.types.FFuncType;
import com.samsthenerd.beedev.language.types.FMetaVar;
import com.samsthenerd.beedev.language.types.FTypeVar;
import com.samsthenerd.beedev.utils.Unit;

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
                   FExpr body) implements FFunc {

        // TODO: need like, skolo somethin here?
//
//        public FExpr apply(FContext ctx, FExpr arg) {
//            return expr().substitute(ctx, argSym, arg);
//        }


        @Override
        public TcMonad<Unit> typeCheck(TcExpected expected) {
            // assuming alpha renaming has already happened
            return switch (expected) {
                case TcCheck(FType type) -> TcMonad.unifyFun(type).bind(ftype ->
                    TcMonad.extendVars(arg, ftype.fromType(), body.checkType(ftype.toType()))
                );
                case TcInfer(FMetaVar mv) -> {
                    FType argVT = FMetaVar.makeNew();
                    yield TcMonad.extendVars(arg, argVT, body.synthType())
                        .bind(bodyTy -> TcMonad.write(mv, new FFuncType(argVT, bodyTy)));
                }
            };
        }

        @Override
        public String sfgString(){
            return "\\" + arg.sfgString() + " -> " + body.sfgString();
        }
    }

    record FPrimFunc(FType annot,
                     Function<FExpr, FExpr> f, CombSym id) implements FFunc {

//        @Override
//        public FExpr apply(FContext ctx, FExpr arg) {
//            return f.apply(arg);
//        }

        public String sfgString(){
            return id.toString();
        }

        @Override
        public TcMonad<Unit> typeCheck(TcExpected expected) {
            return null;
        }

        @Override
        public String debugString() {
            return "primFunc[" + annot + "]@" + hashCode();
        }

//        public FExpr substitute(FContext ctx, CombSym sym, FExpr withExpr) {
//            return this;
//        }

//        @Override
//        public Optional<SynthRes> synthType(TypeEnv env) {
//            return Optional.of(new SynthRes(annot, env));
//        }
    }

    static FFunc binaryPrimFunc(FType t1, FType t2, FType resT, BiFunction<FExpr, FExpr, FExpr> func, CombSym id) {
        return new FPrimFunc(new FFuncType(t1, new FFuncType(t2, resT)), (a) -> new FPrimFunc(new FFuncType(t2, resT), (b) -> func.apply(a, b), CombSym.of(id.asString() + "@" + UUID.randomUUID())), id);
    }
}
