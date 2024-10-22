package com.samsthenerd.beedev.language.core;

import com.samsthenerd.beedev.language.CombSym;

import java.util.List;
import java.util.Map;

// a collection of expressions and types that can be imported to an environment
public record FModule(List<String> namespace, Map<CombSym, FExpr> exprBindings, Map<CombSym, FType> typeDefs){}
