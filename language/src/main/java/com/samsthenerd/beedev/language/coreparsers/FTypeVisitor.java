package com.samsthenerd.beedev.language.coreparsers;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.antlr.SystemfBaseVisitor;
import com.samsthenerd.beedev.language.antlr.SystemfParser;
import com.samsthenerd.beedev.language.antlr.SystemfParser.TypeabsContext;
import com.samsthenerd.beedev.language.core.FType;
import com.samsthenerd.beedev.language.core.types.FFuncType;
import com.samsthenerd.beedev.language.core.types.FQuantType;
import com.samsthenerd.beedev.language.core.types.FTypeVar;

public class FTypeVisitor extends SystemfBaseVisitor<FType> {

    public static final FTypeVisitor INSTANCE = new FTypeVisitor();

    @Override
    public FType visitVar(SystemfParser.VarContext ctx){
        return new FTypeVar(CombSym.of(ctx.VAR().getText()));
    }

    @Override
    public FType visitFunctype(SystemfParser.FunctypeContext ctx) {
        return new FFuncType(visitType(ctx.type(0)), visitType(ctx.type(1)));
    }

    @Override
    public FType visitQuanttype(SystemfParser.QuanttypeContext ctx) {
        return new FQuantType(CombSym.of(ctx.var().getText()), visitType(ctx.type()));
//        return new FFuncType(visitType(ctx.type(0)), visitType(ctx.type(1)));
    }
}
