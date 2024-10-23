package com.samsthenerd.beedev.language.core.exprs;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.core.FContext;
import com.samsthenerd.beedev.language.core.FExpr;
import com.samsthenerd.beedev.language.core.FType;
import com.samsthenerd.beedev.language.core.types.FFuncType;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface FFunc extends FExpr {

    FType getArgType();

    // applies the function with the given argument
    FExpr apply(FContext ctx, FExpr arg);

    default boolean isValue() {
        return true;
    }

    default FExpr reduce(FContext ctx) {
        return this;
    }

    default FExpr substitute(FContext ctx, CombSym sym, FExpr withExpr) {
        return this;
    }

    default FExpr subType(FContext ctx, CombSym sym, FType withT) {
        return this;
    }

    record FLambda(FType argType, CombSym argSym,
                   FExpr expr) implements com.samsthenerd.beedev.language.core.exprs.FFunc {
        @Override
        public FType getArgType() {
            return this.argType();
        }

        public FType getType(FContext ctx) {
            return new FFuncType(argType(), expr.getType(ctx));
        }

        public FExpr substitute(FContext ctx, CombSym sym, FExpr withExpr) {
            return sym.equals(argSym()) ? this :
                new FLambda(argType(), sym, expr.substitute(ctx, sym, withExpr));
        }

        public FExpr subType(FContext ctx, CombSym sym, FType withT) {
            return new FLambda(argType().substitute(ctx, sym, withT), argSym(), expr().subType(ctx, sym, withT));
        }

        public FExpr apply(FContext ctx, FExpr arg) {
            return expr().substitute(ctx, argSym, arg);
        }

        @Override
        public String toString() {
            return "\\(" + argSym() + "): " + argType() + " -> " + expr();
        }
    }

    record FPrimFunc(FType fromT, FType toT,
                     Function<FExpr, FExpr> f) implements com.samsthenerd.beedev.language.core.exprs.FFunc {
        @Override
        public FType getArgType() {
            return fromT();
        }

        @Override
        public FType getType(FContext ctx) {
            return new FFuncType(fromT(), toT());
        }

        @Override
        public FExpr apply(FContext ctx, FExpr arg) {
            return f.apply(arg);
        }

        @Override
        public String toString() {
            return "primFunc[" + fromT() + " -> " + toT() + "]@" + hashCode();
        }
    }

    static com.samsthenerd.beedev.language.core.exprs.FFunc binaryPrimFunc(FType t1, FType t2, FType resT, BiFunction<FExpr, FExpr, FExpr> func) {
        return new FPrimFunc(t1, new FFuncType(t2, resT), (a) -> new FPrimFunc(t2, resT, (b) -> func.apply(a, b)));
    }
}
