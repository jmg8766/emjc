package ast;

import ast.expression.*;
import ast.statement.*;
import ast.type.*;
import ast.type.Boolean;
import ast.type.String;

public interface Visitor<R> {

    R visit(TypeIdList n);

    // statement
    R visit(Program n);
    R visit(Print n);
    R visit(Assign n);
    R visit(Skip n);
    R visit(Block n);
    R visit(IfThenElse n);
    R visit(While n);
    R visit(Sidef n);
    R visit(ClassDeclaration n);
    R visit(VarDeclaration n);
    R visit(MethodDeclaration n);
    R visit(MainClassDeclaration n);
    R visit(Return n);

    //type
    R visit(Int n);
    R visit(Boolean n);
    R visit(String n);
    R visit(Array n);

    // expression
    R visit(IntLiteral n);
    R visit(StringLiteral n);
    R visit(StringPlus n);
    R visit(IntPlus n);
    R visit(Minus n);
    R visit(Times n);
    R visit(Division n);
    R visit(Modulo n);
    R visit(Equals n);
    R visit(LessThan n);
    R visit(And n);
    R visit(Or n);
    R visit(Not n);
    R visit(ID n);
    R visit(FunctionCall n);
}