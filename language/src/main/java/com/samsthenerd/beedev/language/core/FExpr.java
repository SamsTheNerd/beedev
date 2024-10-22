package com.samsthenerd.beedev.language.core;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.core.FType.FError;
import com.samsthenerd.beedev.language.core.FType.FFuncType;
import com.samsthenerd.beedev.language.core.FType.FQuantType;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface FExpr {

    // TODO: build error handling into this? or actually to a lot of these
    FType getType(FContext ctx);

    FExpr substitute(FContext ctx, CombSym sym, FExpr withExpr);

    FExpr subType(FContext ctx, CombSym sym, FType withT);

    boolean isValue();

    FExpr reduce(FContext ctx);


    // does this need a type attached to it?
    record FVar(CombSym sym, FType type) implements FExpr {
        public FType getType(FContext ctx){
            return type;
        }

        public FExpr substitute(FContext ctx, CombSym subSym, FExpr withExpr){
            if(sym().equals(subSym)) return withExpr;
            return this;
        }

        public boolean isValue(){
            return false;
        }

        public FExpr reduce(FContext ctx){
            return this;
        }

        public FExpr subType(FContext ctx, CombSym subSym, FType withT){
            return new FVar(sym, type().substitute(ctx, subSym, withT));
        }
    }

    interface FFunc extends FExpr {

        FType getArgType();

        // applies the function with the given argument
        FExpr apply(FContext ctx, FExpr arg);

        default boolean isValue(){
            return true;
        }

        default FExpr reduce(FContext ctx){
            return this;
        }

        default FExpr substitute(FContext ctx, CombSym sym, FExpr withExpr){
            return this;
        }

        default FExpr subType(FContext ctx, CombSym sym, FType withT){
            return this;
        }

        record FLambda(FType argType, CombSym argSym, FExpr expr) implements FFunc{
            @Override
            public FType getArgType() {
                return this.argType();
            }

            public FType getType(FContext ctx){
                return new FFuncType(argType(), expr.getType(ctx));
            }

            public FExpr substitute(FContext ctx, CombSym sym, FExpr withExpr){
                return sym.equals(argSym()) ? this :
                    new FLambda(argType(), sym, expr.substitute(ctx, sym, withExpr));
            }

            public FExpr subType(FContext ctx, CombSym sym, FType withT){
                return new FLambda(argType().substitute(ctx, sym, withT), argSym(), expr().subType(ctx, sym, withT));
            }

            public FExpr apply(FContext ctx, FExpr arg){
                return expr().substitute(ctx, argSym, arg);
            }

            @Override
            public String toString(){
                return "\\(" + argSym() + "): " + argType() + " -> " + expr();
            }
        }

        record FPrimFunc(FType fromT, FType toT, Function<FExpr, FExpr> f) implements FFunc{
            @Override
            public FType getArgType() {
                return fromT();
            }

            @Override
            public FType getType(FContext ctx){
                return new FFuncType(fromT(), toT());
            }

            @Override
            public FExpr apply(FContext ctx, FExpr arg) {
                return f.apply(arg);
            }

            @Override
            public String toString(){
                return "primFunc[" + fromT() + " -> " + toT() + "]@" + hashCode();
            }
        }

        static FFunc binaryPrimFunc(FType t1, FType t2, FType resT, BiFunction<FExpr, FExpr, FExpr> func){
            return new FPrimFunc(t1, new FFuncType(t2, resT), (a) -> new FPrimFunc(t2, resT, (b) -> func.apply(a,b)));
        }
    }

    record FApp(FExpr func, FExpr arg) implements FExpr {
        public FType getType(FContext ctx){
            if(func.getType(ctx) instanceof FFuncType(FType fromT, FType toT)
                && fromT.equals(arg.getType(ctx))
            ){
                return toT;
            }
            return new FError("Could not apply arg: " + arg + " to expression: " + func);
        }

        public FExpr substitute(FContext ctx, CombSym sym, FExpr withExpr){
            return new FApp(func.substitute(ctx, sym, withExpr), arg.substitute(ctx, sym, withExpr));
        }

        public boolean isValue(){
            return false;
        }

        public FExpr reduce(FContext ctx){
            if(func instanceof FFunc ffunc) return ffunc.apply(ctx, arg);
            if(func.isValue()) return this;
            return new FApp(func.reduce(ctx), arg).reduce(ctx);
        }

        public FExpr subType(FContext ctx, CombSym sym, FType withT){
            return new FApp(func().subType(ctx, sym, withT), arg().subType(ctx, sym, withT));
        }

        @Override
        public String toString(){
            return "app(" + func() + "; " + arg() + ")";
        }
    }

    // used to add primitive-ish types. This likely won't be the preferred way to add new types.
    interface FPrimitive extends FExpr {
        FType getType();

        default FType getType(FContext ctx){
            return getType();
        }

        default FExpr substitute(FContext ctx, CombSym sym, FExpr withExpr){
            return this;
        }

        default boolean isValue(){
            return true;
        }

        default FExpr reduce(FContext ctx){
            return this;
        }

        default FExpr subType(FContext ctx, CombSym sym, FType withT){
            return this;
        }
    }

    record FTypeAbs(CombSym absSym, FExpr expr) implements FExpr {
        public FType getType(FContext ctx){
            // TODO: need to check if type of expression is valid-ish here?
            // and also make it like, actually go into the type ? it might just give an error here rn
            return new FQuantType(absSym(), expr().getType(ctx));
        }

        public FExpr substitute(FContext ctx, CombSym sym, FExpr withExpr){
            return new FTypeAbs(absSym(), expr.substitute(ctx, sym ,withExpr));
        }

        public boolean isValue(){
            return true;
        }

        public FExpr reduce(FContext ctx){
            return this;
        }

        public FExpr subType(FContext ctx, CombSym sym, FType withT){
            return new FTypeAbs(absSym(), expr.subType(ctx, sym, withT));
        }

        @Override
        public String toString(){
            return "Lam[" + absSym() + "].(" + expr() + ")";
        }
    }

    record FTypeApp(FType type, FExpr expr) implements FExpr {
        public FType getType(FContext ctx){
            if(expr.getType(ctx) instanceof FQuantType(CombSym faSym, FType typeBody)
                && type().isWellTyped(ctx)
            ){
                return typeBody.substitute(ctx, faSym, type);
            }
            return new FError("Could not apply type arg: " + type() + " in expression: " + expr());
        }

        public FExpr substitute(FContext ctx, CombSym sym, FExpr withExpr){
            return new FTypeApp(type(), expr.substitute(ctx, sym, withExpr));
        }

        public FExpr subType(FContext ctx, CombSym sym, FType withT){
            return new FTypeApp(type().substitute(ctx, sym, withT), expr.subType(ctx, sym, withT));
        }

        public boolean isValue(){
            return false;
        }

        public FExpr reduce(FContext ctx){
            if(expr instanceof FTypeAbs(CombSym absSym, FExpr body)){
                return body.subType(ctx, absSym, type());
            }
            if(expr.isValue()) return this;
            return new FTypeApp(type(), expr().reduce(ctx)).reduce(ctx);
        }

        @Override
        public String toString(){
            return "App[" + type() + "](" + expr() + ")";
        }
    }

//    static Optional<FExpression> substitute(FExpression arg, )
}
