package com.samsthenerd.beedev.language;

import com.samsthenerd.beedev.language.corelib.PrimInt;
import com.samsthenerd.beedev.language.exprs.FAnnot;
import com.samsthenerd.beedev.language.exprs.FApp;
import com.samsthenerd.beedev.language.exprs.FFunc.FLambda;
import com.samsthenerd.beedev.language.exprs.FVar;
import com.samsthenerd.beedev.language.sorts.FExpr;
import com.samsthenerd.beedev.language.types.FFuncType;
import com.samsthenerd.beedev.language.types.FQuantType;
import com.samsthenerd.beedev.language.types.FTypeVar;

public class TestPad {

//    static final FExpr primAddTest = new FApp(new FApp(PrimInt.INT_ADD, new PrimInt(1)), new PrimInt(2));

//    static final FExpr genId = new FTypeAbs(CombSym.of("a"),
//        new FLambda(
//            new FTypeVar(CombSym.of("a")),
//            CombSym.of("x"),
//            new FVar(CombSym.of("x"), new FTypeVar(CombSym.of("a")))
//        )
//    );

    public static void main(String[] args){
//        System.out.println("Prim Add Test");
//        System.out.println(primAddTest);
//        System.out.println(primAddTest.reduce(new FContext()));
//        System.out.println(primAddTest.getType(new FContext()));
//
//        System.out.println("\nGeneric ID Test");
//        System.out.println(genId);
//        System.out.println(genId.reduce(new FContext()));
//        FType genIdType = genId.getType(new FContext());
//        System.out.println(genIdType);
//        FExpr genidid = new FTypeApp(genIdType, genId).reduce(new FContext());
//        System.out.println(genidid);
//        System.out.println(new FApp(genidid, genId).reduce(new FContext()));
//
//        System.out.println("Gen ID with prim instance");
//        FExpr primId = new FTypeApp(PrimInt.PRIM_INT_TYPE, genId);
//        System.out.println(primId);
//        System.out.println(primId.getType(new FContext()));
//        System.out.println(primId.reduce(new FContext()));
//        System.out.println(primId.reduce(new FContext()).getType(new FContext()));
//        System.out.println(new FApp(primId, new PrimInt(10)).reduce(new FContext()));
//        System.out.println(new FApp(primId, primId).getType(new FContext()));
//        System.out.println(new FApp(primId, primId).reduce(new FContext()));
//
//        SystemfLexer lexer = new SystemfLexer(CharStreams.fromString("ap(lam[a]{x -> x};2)"));
//        SystemfParser parser = new SystemfParser(new CommonTokenStream(lexer));
//        FExpr boopExpr = FExprVisitor.INSTANCE.visitExpr(parser.prog().expr(0));
//        System.out.println(boopExpr);
//        System.out.println(boopExpr.reduce(new FContext()));



//        FExpr annid = new FAnnot(new FLambda(CombSym.of("x"), new FVar(CombSym.of("x"))),
//            new FQuantType(CombSym.of("a"), new FFuncType(new FTypeVar(CombSym.of("a")), new FTypeVar(CombSym.of("a")))));
//
//        System.out.println(annid.synthType(new TypeEnv()).get().type());
//
//        System.out.println(new FApp(annid, new PrimInt(2)));
//        System.out.println(new FApp(annid, new PrimInt(2)).synthType(new TypeEnv()).get().type());




//        runFile("boop.sysf");
    }

    private static void runFile(String filename){
//        InputStream resourceAsStream = TestPad.class.getClassLoader().getResourceAsStream(filename);
//        System.out.println("Testing file: " + filename);
//        try {
//            SystemfLexer lexer = new SystemfLexer(CharStreams.fromStream(resourceAsStream));
//            SystemfParser parser = new SystemfParser(new CommonTokenStream(lexer));
//            for(ExprContext ctx : parser.prog().expr()){
//                FExpr expr = FExprVisitor.INSTANCE.visitExpr(ctx);
//                System.out.println("\noriginal:" + expr.sfgString());
//                System.out.println("type: " + expr.getType(new FContext()).sfgString());
//                System.out.println("reduced: " + expr.reduce(new FContext()).sfgString());
//                System.out.println("type of reduced: " + expr.reduce(new FContext()).getType(new FContext()).sfgString());
//            }
//        } catch (Exception e){
//            System.out.println(e);
//        }
//        FExprVisitor.INSTANCE.visitExpr(parser.prog().expr(0));
    }
}
