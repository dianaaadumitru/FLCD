%{
#include <stdio.h>
#include <stdlib.h>

#define YYDEBUG 1 
%}

%token FUNC
%token ARRAY
%token TEXT
%token NUMBER
%token CONST
%token IF
%token WHILE
%token FOR
%token READ
%token WRITE
%token ELSE


%token IDENTIFIER
%token CONSTANT



%left '=' '+' '-' '*' '/' '==' '<' '>' '%' 'and' 'or' 'not'

%token ASSIGN
%token ADD 
%token SUB
%token DIV 
%token MUL 
%token EQUALS
%token LESS_THAN
%token GREATHER_THAN
%token MODULO
%token AND
%token OR
%token NOT

%token OPEN_CURLY_BRACKET
%token CLOSED_CURLY_BRACKET 
%token OPEN_ROUND_BRACKET
%token CLOSED_ROUND_BRACKET
%token OPEN_RIGHT_BRACKET
%token CLOSED_RIGHT_BRACKET 

%token COMMA 
%token SEMI_COLON
%token EPSILON

%start program

%%

program: FUNC cmpstmt
cmpstmt: OPEN_CURLY_BRACKET stmtlist CLOSED_CURLY_BRACKET
stmtlist: statement | statement stmtlist | EPSILON
statement: simplestmt | structstmt
simplestmt: iostmt | assignstmt | declaration
structstmt: cmpstmt | ifstmt | whilestmt
iostmt: READ OPEN_ROUND_BRACKET IDENTIFIER CLOSED_ROUND_BRACKET SEMI_COLON
iostmt: WRITE OPEN_ROUND_BRACKET outputvariables CLOSED_ROUND_BRACKET SEMI_COLON
assignstmt: IDENTIFIER ASSIGN expression
declaration: arraydecl | primitivedecl
condition: expression relation expression
ifstmt: IF OPEN_ROUND_BRACKET condition CLOSED_ROUND_BRACKET OPEN_CURLY_BRACKET statement CLOSED_CURLY_BRACKET ELSE OPEN_CURLY_BRACKET statement CLOSED_CURLY_BRACKET
whilestmt: WHILE OPEN_ROUND_BRACKET condition CLOSED_ROUND_BRACKET cmpstmt
outputvariables: IDENTIFIER
outputvariables: CONSTANT
expression: term
arraydecl: simpletype ARRAY IDENTIFIER
primitivedecl: simpletype IDENTIFIER SEMI_COLON
simpletype: NUMBER | TEXT
term: factor
factor: OPEN_ROUND_BRACKET expression CLOSED_ROUND_BRACKET
factor: IDENTIFIER
relation: LESS_THAN
relation: GREATHER_THAN

%%



yyerror(char *s)
{
  printf("%s\n", s);
}

extern FILE *yyin;

main(int argc, char **argv)
{
  if(argc>1) yyin = fopen(argv[1], "r");
  if((argc>2)&&(!strcmp(argv[2],"-d"))) yydebug = 1;
  if(!yyparse()) fprintf(stderr,"\tO.K.\n");
}