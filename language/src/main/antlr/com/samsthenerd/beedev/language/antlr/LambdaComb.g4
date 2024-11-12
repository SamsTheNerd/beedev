grammar LambdaComb;
@header{package com.samsthenerd.beedev.language.antlr;} // so that it has a sensible package and intellij can read it fine

prog: (expr '\n'?)+ EOF;

expr: var
    | '(' expr ')'
    | literal
    | lam
    | app
    ;

type: var
    | functype
    | quanttype
    ;

var : VAR;

literal: intlit;

intlit: INT;

lam: '\\' var ARROW expr;

app: '(' expr expr ')';

functype: '(' type '->' type ')';

quanttype: 'all[' var ';' type ']';


ARROW: '->';
INT: [0-9]+;
VAR: [a-z] [a-zA-Z0-9]*;
WS : (' '|'\t'|'\n')+ -> skip;


//SPACE :       (' '|'\t')* -> channel(HIDDEN) ;

//ARROW : SPACE '->' SPACE;
//LParen : SPACE '(' SPACE;
//RParen : SPACE ')' SPACE;

//grammar lambda;
//
//prog
//    : expression EOF
//    ;
//
//expression
//    : var
//    | function_
//    | application
//    ;
//
//var: VARIABLE;
//
//function_
//    : '\\' var '.' scope
//    ;
//
//application
//    : '(' expression expression ')'
//    ;
//
//scope
//    : expression
//    ;
//
//VARIABLE
//    : [a-z] [a-zA-Z0-9]*
//    ;
//
//WS
//    : [ \t\r\n] -> skip
//    ;