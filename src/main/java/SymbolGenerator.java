import ast.Binding;
import ast.ID;
import ast.TypeIdList;
import ast.Visitor;
import ast.expression.*;
import ast.expression.operators.*;
import ast.statement.*;
import ast.type.Boolean;
import ast.type.Int;
import ast.type.IntArray;
import ast.type.String;

import java.util.Hashtable;
import java.util.Stack;

public class SymbolGenerator implements Visitor {

	Hashtable<ID, Binding> symbols = new Hashtable<>();
	Stack<ID> ids;

	@Override
	public Object visit(TypeIdList n) {
		return null;
	}

	@Override
	public Object visit(Program n) {
		return null;
	}

	@Override
	public Object visit(Print n) {
		return null;
	}

	@Override
	public Object visit(Assign n) {
		return null;
	}

	@Override
	public Object visit(Block n) {
		return null;
	}

	@Override
	public Object visit(IfThenElse n) {
		return null;
	}

	@Override
	public Object visit(While n) {
		return null;
	}

	@Override
	public Object visit(Sidef n) {
		return null;
	}

	@Override
	public Object visit(ClassDeclaration n) {
		return null;
	}

	@Override
	public Object visit(VarDeclaration n) {
		return null;
	}

	@Override
	public Object visit(MethodDeclaration n) {
		return null;
	}

	@Override
	public Object visit(MainClassDeclaration n) {
		return null;
	}

	@Override
	public Object visit(Return n) {
		return null;
	}

	@Override
	public Object visit(Int n) {
		return null;
	}

	@Override
	public Object visit(Boolean n) {
		return null;
	}

	@Override
	public Object visit(String n) {
		return null;
	}

	@Override
	public Object visit(IntArray n) {
		return null;
	}

	@Override
	public Object visit(NewObject n) {
		return null;
	}

	@Override
	public Object visit(NewArray n) {
		return null;
	}

	@Override
	public Object visit(Precedence n) {
		return null;
	}

	@Override
	public Object visit(ArrayIndex n) {
		return null;
	}

	@Override
	public Object visit(This n) {
		return null;
	}

	@Override
	public Object visit(BooleanLiteral n) {
		return null;
	}

	@Override
	public Object visit(IntLiteral n) {
		return null;
	}

	@Override
	public Object visit(StringLiteral n) {
		return null;
	}

	@Override
	public Object visit(Plus n) {
		return null;
	}

	@Override
	public Object visit(Minus n) {
		return null;
	}

	@Override
	public Object visit(Times n) {
		return null;
	}

	@Override
	public Object visit(Division n) {
		return null;
	}

	@Override
	public Object visit(Equals n) {
		return null;
	}

	@Override
	public Object visit(LessThan n) {
		return null;
	}

	@Override
	public Object visit(And n) {
		return null;
	}

	@Override
	public Object visit(Or n) {
		return null;
	}

	@Override
	public Object visit(Not n) {
		return null;
	}

	@Override
	public Object visit(ID n) {
		return null;
	}

	@Override
	public Object visit(FunctionCall n) {
		return null;
	}

	@Override
	public Object visit(Length n) {
		return null;
	}
}
