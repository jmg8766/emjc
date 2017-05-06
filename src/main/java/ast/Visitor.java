package ast;

import ast.ClassDeclaration.ClassDeclExtends;
import ast.ClassDeclaration.ClassDeclSimple;
import ast.expression.*;
import ast.statement.*;
import ast.type.*;

public interface Visitor<R> {
	 R visit(Program n);
	R visit(MainClass n);
	R visit(ClassDeclSimple n);
	R visit(ClassDeclExtends n);
	R visit(VarDecl n);
	R visit(MethodDecl n);
	R visit(Formal n);
	R visit(IntArrayType n);
	R visit(BooleanType n);
	R visit(IntegerType n);
	R visit(StringType n);
	R visit(IdentifierType n);
	R visit(Block n);
	R visit(If n);
	R visit(While n);
	R visit(Print n);
	R visit(Assign n);
	R visit(ArrayAssign n);
	R visit(And n);
	R visit(Or n);
	R visit(LessThan n);
	R visit(Equals n);
	R visit(Plus n);
	R visit(Minus n);
	R visit(Times n);
	R visit(Divide n);
	R visit(ArrayLookup n);
	R visit(ArrayLength n);
	R visit(Call n);
	R visit(IntegerLiteral n);
	R visit(StringLiteral n);
	R visit(True n);
	R visit(False n);
	R visit(IdentifierExp n);
	R visit(This n);
	R visit(NewArray n);
	R visit(NewObject n); R visit(Not n);
	R visit(Identifier n);

    R visit(Sidef sidef);
}
