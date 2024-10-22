package com.samsthenerd.beedev.language.core;

import com.samsthenerd.beedev.language.CombSym;

public interface FType {

    boolean isRank(FContext ctx, int k);

    boolean isWellTyped(FContext ctx);

    FType substitute(FContext ctx, CombSym sym, FType withT);

    record FTypeVar(CombSym sym) implements FType{
        public boolean isRank(FContext ctx, int k) {
            // mm depends on ??
            return true; // idk
        }

        public boolean isWellTyped(FContext ctx){
            return ctx.getBoundType(sym()).map(t -> t.isWellTyped(ctx)).orElse(false);
        }

        public FType substitute(FContext ctx, CombSym symS, FType withT){
            return symS.equals(sym()) ? withT : this;
        }

        public String toString(){
            return "T[" + sym.toString() + "]";
        }
    }

    record FFuncType(FType fromType, FType toType) implements FType{
        public boolean isRank(FContext ctx, int k){
            if(k == 0) return fromType.isRank(ctx, 0) && fromType.isRank(ctx, 0);
            return fromType.isRank(ctx, k-1) && toType.isRank(ctx, k);
        }

        public boolean isWellTyped(FContext ctx){
            return fromType().isWellTyped(ctx) && toType().isWellTyped(ctx);
        }

        public FType substitute(FContext ctx, CombSym sym, FType withT){
            return new FFuncType(fromType().substitute(ctx, sym, withT), toType().substitute(ctx, sym, withT));
        }

        @Override
        public String toString(){
            return fromType() + " -> " + toType();
        }
    }

    record FQuantType(CombSym faSym, FType typeBody) implements FType{
        public boolean isRank(FContext ctx, int rank){
            return true;
        }

        public boolean isWellTyped(FContext ctx){
            return typeBody().isWellTyped(ctx); // this doesn't seem,, complete?
        }

        public FType substitute(FContext ctx, CombSym sym, FType withT){
            // assume alpha renaming prevents capturing here?
            // also TODO: rank restriction here ?
            return new FQuantType(sym, withT.substitute(ctx, sym, withT));
        }

        @Override
        public String toString(){
            return "all[" + faSym() + "." + typeBody + "]";
        }
    }

    // used to indicate an error in type checking
    record FError(String eMsg) implements FType{
        public boolean isRank(FContext ctx, int rank){
            return true;
        }

        public boolean isWellTyped(FContext ctx){
            return false;
        }

        public FType substitute(FContext ctx, CombSym sym, FType withT){
            return this;
        }
    }

    // so that we can test stuff ? see if needed
//    record FTrivial() implements FType{
//        public boolean isRank(FContext ctx, int rank){
//            return true;
//        }
//
//        public boolean isWellTyped(FContext ctx){
//            return true;
//        }
//
//        public FType substitute(FContext ctx, CombSym sym, FType withT){
//            return this;
//        }
//    }

    // sure?
    record FPrimType(CombSym name) implements FType{
        // force prims into being rank 0 ?
        public boolean isRank(FContext ctx, int k){ return true; }

        public boolean isWellTyped(FContext ctx){
            return true; // TODO: maybe figure out like, a module sorta system that can enable/disable prims
        }

        public FType substitute(FContext ctx, CombSym sym, FType withT){
            return this;
        }

        @Override
        public String toString(){
            return "Prim[" + name + "]";
        }
    }

    // replace all instances of vT in bodyT with argT
//    static Optional<FType> substitute(FType argT, FTypeVar vT, FType bodyT){
//        switch (bodyT){
//            case FTypeVar v2 -> {
//                return Optional.of(v2.equals(vT) ? argT : v2);
//            }
//            case FFuncType(FType fromT, FType toT) -> {
//                Optional<FType> maybeFT = substitute(argT, vT, fromT);
//                Optional<FType> maybeTT = substitute(argT, vT, toT);
//                if(maybeFT.isPresent() && maybeTT.isPresent())
//                    return Optional.of(new FFuncType(maybeFT.get(), maybeTT.get()));
//                return Optional.empty();
//            }
//            case FQuantType(FTypeVar typeVar, FType typeBody) -> {
//                // TODO: figure out rank restrictions. also better error handling
//                return Optional.empty();
//            }
//            default -> {return Optional.empty(); }
//        }
//    }

//    default Optional<FType> substitute(FType argT, FTypeVar vT){
//        return FType.substitute(argT, vT, this);
//    }
}
