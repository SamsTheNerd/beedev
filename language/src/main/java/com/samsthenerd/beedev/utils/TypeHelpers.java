package com.samsthenerd.beedev.utils;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.TcMonad;
import com.samsthenerd.beedev.language.sorts.FType;
import com.samsthenerd.beedev.language.types.FFuncType;
import com.samsthenerd.beedev.language.types.FMetaVar;
import com.samsthenerd.beedev.language.types.FQuantType;
import com.samsthenerd.beedev.language.types.FTypeVar;

import java.util.*;

public class TypeHelpers {

//    // pulls any quantifiers out
//    public static Prenexed getPrenexedIdiot(FType type){
//        if(type instanceof FQuantType(CombSym faSym, FType typeBody)){
//            Prenexed recnex = getPrenexedIdiot(typeBody);
//            List<CombSym> syms = recnex.syms();
//            syms.addFirst(faSym);
//            return new Prenexed(syms, recnex.rhotype());
//        }
//        return new Prenexed(new LinkedList<>(), type);
//    }
//
//    public record Prenexed(List<CombSym> syms, FType rhotype){
//        public FType instantiate(){
//            FType newtype = rhotype;
//            for(CombSym sym : syms){
//                newtype = newtype.substitute(sym, new FTypeVar(CombSym.arbitrary()));
//            }
//            return newtype;
//        }
//    }

    // model here is going to be handle most stuff in here because I like that concentrated pattern
    // and then expose sensible hooks for everything in an interface or something so it can be expanded-ish

    // gets all metavars in a type
    public static List<FMetaVar> getMetaVars(FType type){
        if(type instanceof FMetaVar mv){
            return List.of(mv);
        }
        if(type instanceof FFuncType(FType fromType, FType toType)){
            List<FMetaVar> mvs = new ArrayList<>(getMetaVars(fromType));
            mvs.addAll(getMetaVars(toType));
            return mvs;
        }
        if(type instanceof FQuantType(List<FTypeVar> qVars, FType typeBody)){
            return getMetaVars(typeBody);
        }
        return List.of();
        // TODO: use an accumulator?
    }

    public static List<FTypeVar> getFreeTypeVars(FType type, Set<FTypeVar> bound){
        if(type instanceof FTypeVar mv){
            if(!bound.contains(mv)) return List.of(mv);
            return List.of();
        }
        if(type instanceof FFuncType(FType fromType, FType toType)){
            List<FTypeVar> mvs = new ArrayList<>(getFreeTypeVars(fromType, bound));
            mvs.addAll(getFreeTypeVars(toType, bound));
            return mvs;
        }
        if(type instanceof FQuantType(List<FTypeVar> qVars, FType typeBody)){
            Set<FTypeVar> newBounds = new HashSet<>(bound);
            newBounds.addAll(qVars);
            return getFreeTypeVars(typeBody, newBounds);
        }
        return List.of();
        // TODO: use an accumulator? or atleast not a set like this. actually maybe these should just be directly in the tcmonad idk
    }

    public static FType canonicalize(FType type){
        if(type instanceof FQuantType(List<FTypeVar> qVars, FType typeBody)){
            int n = 1;
            Hamt<FTypeVar, FType> subst = Hamt.empty();
            List<FTypeVar> newBinders = new ArrayList<>();
            for(var tv : qVars){
                var nv = new FTypeVar(CombSym.of("t" + n));
                subst = subst.assoc(tv, nv);
                newBinders.add(nv);
                n++;
            }
            return new FQuantType(newBinders, typeBody.substitute(subst));
        }
        return type;
    }
}
