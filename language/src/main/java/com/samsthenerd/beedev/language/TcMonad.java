package com.samsthenerd.beedev.language;

import com.samsthenerd.beedev.language.exprs.FVar;
import com.samsthenerd.beedev.language.sorts.FType;
import com.samsthenerd.beedev.language.types.*;
import com.samsthenerd.beedev.utils.Either;
import com.samsthenerd.beedev.utils.Either.Left;
import com.samsthenerd.beedev.utils.Either.Right;
import com.samsthenerd.beedev.utils.Hamt;
import com.samsthenerd.beedev.utils.TypeHelpers;
import com.samsthenerd.beedev.utils.Unit;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

// helps structure our type inference/checking. based on TcMonad from that one paper.
public class TcMonad<T> {
    // consider these to be partial until execution
    private final TcPayload<Either<T, String>> payload;

    private TcMonad(TcPayload<Either<T, String>> input){
        payload = input;
    }

    // evTMs will be written to
    public Either<T, String> eval(Map<FMetaVar, FType> evTMs, Hamt<FVar, FType> evVTs){
        return payload.eval(evTMs, evVTs);
    }

    public static <T> TcMonad<T> of(T input){
        return new TcMonad<T>(TcPayload.wrap(new Left<>(input)));
    }

    public static <U> TcMonad<U> error(String err){
        return new TcMonad<>(TcPayload.wrap(new Right<>(err)));
    }

    public <S> TcMonad<S> bind(Function<T, TcMonad<S>> func){
        return new TcMonad<>((te, ve) -> switch (payload.eval(te, ve)) {
                case Left(T contents) -> func.apply(contents).eval(te, ve);
                case Right(var err) -> new Right<>(err);
            });
    }

    public <S> TcMonad<S> seq(TcMonad<S> next){
        return bind(x -> next);
    }

    public <S> TcMonad<S> fmap(Function<T, S> map){
        return new TcMonad<>((te, ve) -> payload.eval(te, ve).map(map, Function.identity()));
    }

    public <S> TcMonad<S> with(S newPayload){
        return fmap(x -> newPayload);
    }

    public TcMonad<Unit> drop(){
        return with(Unit.UNIT);
    }

    public static TcMonad<Unit> unit(){
        return of(Unit.UNIT);
    }

    public static TcMonad<Unit> write(FMetaVar meta, FType type){
        return new TcMonad<>((te, ve) -> {
            te.put(meta, type);
            return new Left<>(Unit.UNIT);
        });
    }

    public static TcMonad<Optional<FType>> read(FMetaVar meta){
        return new TcMonad<>((te, ve) -> new Left<>(Optional.ofNullable(te.get(meta))));
    }

    // run an action in the extended scope
    public static <S> TcMonad<S> extendVars(FVar var, FType type, TcMonad<S> scoped){
        return new TcMonad<>((te, ve) -> scoped.eval(te, ve.assoc(var, type)));
//        return new TcMonad<>(new Unit(), typeMappingsSubsts, varTypingsSubsts.assoc(var, type), errorMsg);
    }

    public static TcMonad<Optional<FType>> lookupVar(FVar var){
        return new TcMonad<>((te, ve) -> new Left<>(ve.get(var)));
    }

    public static TcMonad<FType> lookupVarStrict(FVar var){
        TcMonad<Optional<FType>> tcl = lookupVar(var);
        return tcl.bind(opt -> opt.isPresent() ? tcl.with(opt.get()) : error("Not in scope: " + var));
    }

    // assume these are monotypes ?
    public static TcMonad<Unit> unify(FType t1, FType t2){
        if(t1 instanceof FTypeVar && t2 instanceof FTypeVar && t1.equals(t2)) return unit();
        if(t1 instanceof FMetaVar && t2 instanceof FMetaVar && t1.equals(t2)) return unit();
        if(t1 instanceof FMetaVar mv) return unifyVar(mv, t2);
        if(t2 instanceof FMetaVar mv) return unifyVar(mv, t1);

        if(t1 instanceof FFuncType(FType frT1, FType toT1) && t2 instanceof FFuncType(FType frT2, FType toT2)){
            return unify(frT1, frT2).seq(unify(toT1, toT2));
        }

        if(t1 instanceof FPrimType && t2 instanceof FPrimType && t1.equals(t2)) return unit();

        return error("Could not unify types " + t1 + " and " + t2); // TODO: make better errors
    }

    private static TcMonad<Unit> unifyVar(FMetaVar mv, FType ty2){
        // check if mv is bound or not
        return read(mv)
            .bind(opt -> opt
                .map(ty1 -> unify(ty1, ty2)) // unify bound
                .orElse(unifyUnboundVar(mv, ty2)) // or unify unbound
            );
    }

    // mv1 is an unbound meta var
    private static TcMonad<Unit> unifyUnboundVar(FMetaVar mv1, FType ty2) {
        if(ty2 instanceof FMetaVar mv2){ // case where ty2 is also a metavar
            // check if mv2/ty2 is bound or not
            return read(mv2)
                .bind(opt -> opt
                    .map(ty2b -> unify(mv1, ty2b)) // is bound, try to unify with the unbound metavar
                    .orElse(write(mv1, ty2)) // neither is bound, write ty2 to mv1 ? for some reason?
                );
        }
        // ty2 is not a metavar
        return getMetaVars(List.of(ty2)).bind(mvs -> {
            if (mvs.contains(mv1)){
                return error("Type " + mv1 + " occurs in type " + ty2);
            } else {
                return write(mv1, ty2);
            }
        });
    }

    // gets all types in the environment ?
    public static TcMonad<List<FType>> getEnvTypes(){
        return new TcMonad<>((te, ve) -> new Left<>(ve.stream().map(Entry::getValue).toList()));
    }

    // gets all unbound meta vars in the given types
    public static TcMonad<Set<FMetaVar>> getMetaVars(List<FType> inTypes){
        // TODO: zonking ??
        return of(inTypes.stream().map(TypeHelpers::getMetaVars).reduce(new HashSet<>(), (a,b) -> {a.addAll(b); return a;}));
    }

    public static TcMonad<Set<FTypeVar>> getFreeTypeVars(List<FType> inTypes){
        return of(inTypes.stream().map(t -> TypeHelpers.getFreeTypeVars(t, new HashSet<>())).reduce(new HashSet<>(), (a,b) -> {a.addAll(b); return a;}));
    }

    @FunctionalInterface
    public interface TcPayload<T>{
        // map is explicitly mutable and global but shouldn't be exposed much
        T eval(Map<FMetaVar, FType> typeEnv, Hamt<FVar, FType> varEnv);

        static <T> TcPayload<T> wrap(T cPayload){
            return (a,b) -> cPayload;
        }
    }
}
