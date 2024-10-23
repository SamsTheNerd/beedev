package com.samsthenerd.beedev.language;

import com.samsthenerd.beedev.language.corelib.PrimInt;
import com.samsthenerd.beedev.language.core.FContext;
import com.samsthenerd.beedev.language.core.FExpr;
import com.samsthenerd.beedev.language.core.exprs.FApp;
import com.samsthenerd.beedev.language.core.exprs.FFunc.FLambda;
import com.samsthenerd.beedev.language.core.exprs.FTypeAbs;
import com.samsthenerd.beedev.language.core.exprs.FTypeApp;
import com.samsthenerd.beedev.language.core.exprs.FVar;
import com.samsthenerd.beedev.language.core.FType;
import com.samsthenerd.beedev.language.core.types.FTypeVar;

public class TestPad {

    static final FExpr primAddTest = new FApp(new FApp(PrimInt.INT_ADD, new PrimInt(1)), new PrimInt(2));

    static final FExpr genId = new FTypeAbs(CombSym.of("a"),
        new FLambda(
            new FTypeVar(CombSym.of("a")),
            CombSym.of("x"),
            new FVar(CombSym.of("x"), new FTypeVar(CombSym.of("a")))
        )
    );

    public static void main(String[] args){
        System.out.println("Prim Add Test");
        System.out.println(primAddTest);
        System.out.println(primAddTest.reduce(new FContext()));
        System.out.println(primAddTest.getType(new FContext()));

        System.out.println("\nGeneric ID Test");
        System.out.println(genId);
        System.out.println(genId.reduce(new FContext()));
        FType genIdType = genId.getType(new FContext());
        System.out.println(genIdType);
        FExpr genidid = new FTypeApp(genIdType, genId).reduce(new FContext());
        System.out.println(genidid);
        System.out.println(new FApp(genidid, genId).reduce(new FContext()));

        System.out.println("Gen ID with prim instance");
        FExpr primId = new FTypeApp(PrimInt.PRIM_INT_TYPE, genId);
        System.out.println(primId);
        System.out.println(primId.getType(new FContext()));
        System.out.println(primId.reduce(new FContext()));
        System.out.println(primId.reduce(new FContext()).getType(new FContext()));
        System.out.println(new FApp(primId, new PrimInt(10)).reduce(new FContext()));
        System.out.println(new FApp(primId, primId).getType(new FContext()));
        System.out.println(new FApp(primId, primId).reduce(new FContext()));
    }
}
