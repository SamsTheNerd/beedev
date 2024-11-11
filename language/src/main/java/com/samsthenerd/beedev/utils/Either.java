package com.samsthenerd.beedev.utils;

import java.util.Optional;
import java.util.function.Function;

public sealed interface Either<L, R>{

    Optional<L> left();
    Optional<R> right();

    default L leftOrElse(L val){return left().orElse(val);}
    default R rightOrElse(R val){return right().orElse(val);}

    <LS, RS> Either<LS, RS> map(Function<? super L, ? extends LS> leftMap, Function<? super R, ? extends RS> rightMap);

    <LS, RS> Either<LS, RS> flatMap(Function<? super L, ? extends Either<? extends LS, ? extends RS>> leftMap,
                                    Function<? super R, ? extends Either<? extends LS, ? extends RS>> rightMap);

    <U> U reduce(Function<? super L, ? extends U> leftMap, Function<? super R, ? extends U> rightMap);

    record Left<L, R>(L contents) implements Either<L, R>{
        @Override
        public Optional<L> left(){
            return Optional.of(contents);
        }

        @Override
        public Optional<R> right(){
            return Optional.empty();
        }

        @Override
        public <LS, RS> Either<LS, RS> map(Function<? super L, ? extends LS> leftMap, Function<? super R, ? extends RS> rightMap){
            return new Left<>(leftMap.apply(contents));
        }

        @Override
        @SuppressWarnings("unchecked")
        public <LS, RS> Either<LS, RS> flatMap(Function<? super L, ? extends Either<? extends LS, ? extends RS>> leftMap,
                                        Function<? super R, ? extends Either<? extends LS, ? extends RS>> rightMap){
            return (Either<LS, RS>) leftMap.apply(contents);
        }

        @Override
        public <U> U reduce(Function<? super L, ? extends U> leftMap, Function<? super R, ? extends U> rightMap){
            return leftMap.apply(contents);
        }
    }

    record Right<L, R>(R contents) implements Either<L, R>{
        @Override
        public Optional<L> left(){
            return Optional.empty();
        }

        @Override
        public Optional<R> right(){
            return Optional.of(contents);
        }

        @Override
        public <LS, RS> Either<LS, RS> map(Function<? super L, ? extends LS> leftMap, Function<? super R, ? extends RS> rightMap){
            return new Right<>(rightMap.apply(contents));
        }

        @Override
        @SuppressWarnings("unchecked")
        public <LS, RS> Either<LS, RS> flatMap(Function<? super L, ? extends Either<? extends LS, ? extends RS>> leftMap,
                                               Function<? super R, ? extends Either<? extends LS, ? extends RS>> rightMap){
            return (Either<LS, RS>) rightMap.apply(contents);
        }

        @Override
        public <U> U reduce(Function<? super L, ? extends U> leftMap, Function<? super R, ? extends U> rightMap){
            return rightMap.apply(contents);
        }
    }
}
