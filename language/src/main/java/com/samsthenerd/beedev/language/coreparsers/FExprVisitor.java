package com.samsthenerd.beedev.language.coreparsers;

import com.samsthenerd.beedev.language.CombSym;
import com.samsthenerd.beedev.language.antlr.SystemfBaseVisitor;
import com.samsthenerd.beedev.language.antlr.SystemfParser;
import com.samsthenerd.beedev.language.sorts.FExpr;
import com.samsthenerd.beedev.language.exprs.*;
import com.samsthenerd.beedev.language.exprs.FFunc.FLambda;
import com.samsthenerd.beedev.language.types.FError;
import com.samsthenerd.beedev.language.corelib.PrimInt;

public class FExprVisitor extends SystemfBaseVisitor<FExpr> {

    public static final FExprVisitor INSTANCE = new FExprVisitor();

    @Override public FExpr visitVar(SystemfParser.VarContext ctx) {
        return new FVar(CombSym.of(ctx.VAR().getText()));
    }

    @Override
    public FExpr visitLam(SystemfParser.LamContext ctx) {
        return new FLambda(
            CombSym.of(ctx.var().VAR().getText()),
            visitExpr(ctx.expr())
        );
    }

    @Override
    public FExpr visitApp(SystemfParser.AppContext ctx) {
        return new FApp(visitExpr(ctx.expr(0)), visitExpr(ctx.expr(1)));
    }

//    @Override
//    public FExpr visitTypeabs(SystemfParser.TypeabsContext ctx) {
//        return new FTypeAbs(CombSym.of(ctx.var().VAR().getText()), visitExpr(ctx.expr()));
//    }

//    @Override
//    public FExpr visitTypeapp(SystemfParser.TypeappContext ctx) {
//        return new FTypeApp(FTypeVisitor.INSTANCE.visitType(ctx.type()), visitExpr(ctx.expr()));
//    }

    @Override
    public FExpr visitIntlit(SystemfParser.IntlitContext ctx) {
        return new PrimInt(Integer.parseInt(ctx.getText()));
    }
}
