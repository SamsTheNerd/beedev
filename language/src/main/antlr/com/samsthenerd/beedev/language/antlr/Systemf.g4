grammar Systemf;
@header{package com.samsthenerd.beedev.language.antlr;} // so that it has a sensible package and intellij can read it fine

prog: (expr '\n'?)+ EOF;

expr: var
    | literal
    | lam
    | app
    | typeabs
    | typeapp
    ;

type: var
    | functype
    | quanttype
    ;

var : VAR;

literal: intlit;

intlit: INT;

lam: 'lam[' type ']' '{' var '->' expr '}';

app: 'ap(' expr ';' expr ')';

typeabs: 'Lam[' var ';' expr ']';

typeapp: 'App[' type ']' '(' expr ')';

functype: '(' type '->' type ')';

quanttype: 'all[' var ';' type ']';

VAR: [a-z] [a-zA-Z0-9]*;

INT: [0-9]+;

WS : (' ' | '\t' | '\n')+ -> channel(HIDDEN);

//prog:	expr EOF ;
//expr:	expr ('*'|'/') expr
//    |	expr ('+'|'-') expr
//    |	INT
//    |	'(' expr ')'
//    ;
//NEWLINE : [\r\n]+ -> skip;
//INT     : [0-9]+ ;
