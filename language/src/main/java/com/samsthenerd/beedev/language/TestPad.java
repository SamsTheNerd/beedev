package com.samsthenerd.beedev.language;

import com.samsthenerd.beedev.language.antlr.LambdaCombLexer;
import com.samsthenerd.beedev.language.antlr.LambdaCombParser;
import com.samsthenerd.beedev.language.antlr.LambdaCombParser.ExprContext;
import com.samsthenerd.beedev.language.antlr.SystemfLexer;
import com.samsthenerd.beedev.language.antlr.SystemfParser;
import com.samsthenerd.beedev.language.corelib.PrimInt;
import com.samsthenerd.beedev.language.coreparsers.FExprVisitor;
import com.samsthenerd.beedev.language.exprs.FAnnot;
import com.samsthenerd.beedev.language.exprs.FApp;
import com.samsthenerd.beedev.language.exprs.FFunc.FLambda;
import com.samsthenerd.beedev.language.exprs.FVar;
import com.samsthenerd.beedev.language.sorts.FExpr;
import com.samsthenerd.beedev.language.sorts.FType;
import com.samsthenerd.beedev.language.types.FFuncType;
import com.samsthenerd.beedev.language.types.FQuantType;
import com.samsthenerd.beedev.language.types.FTypeVar;
import com.samsthenerd.beedev.utils.Either;
import com.samsthenerd.beedev.utils.Hamt;
import com.samsthenerd.beedev.utils.TcHelpers;
import com.samsthenerd.beedev.utils.TypeHelpers;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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

        FExpr id = new FLambda(new FVar(CombSym.of("x")), new FVar(CombSym.of("x")));
        FExpr churchTrue = new FLambda(
            new FVar(CombSym.of("x")),
            new FLambda(
                new FVar(CombSym.of("y")),
                new FVar(CombSym.of("x")))
            );
        FExpr churchFalse = new FLambda(
            new FVar(CombSym.of("x")),
            new FLambda(
                new FVar(CombSym.of("y")),
                new FVar(CombSym.of("y")))
            );

        System.out.println(id);
        System.out.println(id.synthType().eval(new HashMap<>(), Hamt.empty()));
        System.out.println(churchTrue);
        System.out.println(churchTrue.synthType().eval(new HashMap<>(), Hamt.empty()));
        System.out.println(churchFalse);
        System.out.println(churchFalse.synthType().eval(new HashMap<>(), Hamt.empty()));

        System.out.println(new PrimInt(2).synthType().eval(new HashMap<>(), Hamt.empty()));
        FExpr idInt = new FApp(id, new PrimInt(3));
//        FExpr idInt = new FLambda(new FVar(CombSym.of("y")), new FApp(id, new FVar(CombSym.of("y"))));
        System.out.println(idInt);
        System.out.println(idInt.synthType().bind(TcMonad::zonkType).eval(new HashMap<>(), Hamt.empty()));

        FVar xVar = new FVar(CombSym.of("x"));
        FVar yVar = new FVar(CombSym.of("y"));
        FVar zVar = new FVar(CombSym.of("z"));

        FExpr sComb = new FLambda(xVar, new FLambda(yVar, new FLambda(zVar,
            new FApp(
                new FApp(xVar, zVar),
                new FApp(yVar, zVar))
        )));

        System.out.println(sComb);
        System.out.println(TcHelpers.inferPoly(sComb).fmap(TypeHelpers::canonicalize).eval(new HashMap<>(), Hamt.empty()));
        var sCombIdType = TcHelpers.inferPoly((new FApp(sComb, id))).eval(new HashMap<>(), Hamt.empty());
        System.out.println(sCombIdType.lMap(TypeHelpers::canonicalize));

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

        runFile("boop.lcomb");
    }

    private static void runFile(String filename){
        InputStream resourceAsStream = TestPad.class.getClassLoader().getResourceAsStream(filename);
        System.out.println("Testing file: " + filename);
        try {
            LambdaCombLexer lexer = new LambdaCombLexer(CharStreams.fromStream(resourceAsStream));
            LambdaCombParser parser = new LambdaCombParser(new CommonTokenStream(lexer));
            for(ExprContext ctx : parser.prog().expr()){
                FExpr expr = FExprVisitor.INSTANCE.visitExpr(ctx);
                var typeRes = TcHelpers.inferPoly(expr).fmap(TypeHelpers::canonicalize).eval(new HashMap<>(), Hamt.empty());
                String typePrint = typeRes.reduce((Object::toString), Function.identity());
                System.out.println(expr.sfgString() + " :: " + typePrint);
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
