package com.samsthenerd.beedev.language.coreparsers;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.antlr.LambdaCombBaseVisitor;
import com.samsthenerd.beedev.language.antlr.LambdaCombParser;
import com.samsthenerd.beedev.language.antlr.SystemfBaseVisitor;
import com.samsthenerd.beedev.language.antlr.SystemfParser;
import com.samsthenerd.beedev.language.sorts.FType;
import com.samsthenerd.beedev.language.types.FFuncType;
import com.samsthenerd.beedev.language.types.FQuantType;
import com.samsthenerd.beedev.language.types.FTypeVar;

import java.util.List;

public class FTypeVisitor extends LambdaCombBaseVisitor<FType> {

    public static final FTypeVisitor INSTANCE = new FTypeVisitor();

    @Override
    public FType visitVar(LambdaCombParser.VarContext ctx){
        return new FTypeVar(CombSym.of(ctx.VAR().getText()));
    }

    @Override
    public FType visitFunctype(LambdaCombParser.FunctypeContext ctx) {
        return new FFuncType(visitType(ctx.type(0)), visitType(ctx.type(1)));
    }

    @Override
    public FType visitQuanttype(LambdaCombParser.QuanttypeContext ctx) {
        return new FQuantType(
            List.of(new FTypeVar(CombSym.of(ctx.var().getText()))),
            visitType(ctx.type()));
//        return new FFuncType(visitType(ctx.type(0)), visitType(ctx.type(1)));
    }
}
