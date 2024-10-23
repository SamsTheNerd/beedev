package com.samsthenerd.beedev.language.core;

import com.samsthenerd.beedev.language.CombSym;

import java.util.Collection;
import java.util.List;
import java.util.Map;

// a collection of expressions and types that can be imported to an environment
public record FModule(CombSym identifier, Map<CombSym, FExpr> exprBindings,
                      Map<CombSym, FType> typeDefs, Collection<CombSym> dependencies){
    // need to figure out how exactly we want to represent modules and bits of modules and dependencies and all that.
}
