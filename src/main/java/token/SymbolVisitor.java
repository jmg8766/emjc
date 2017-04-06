package token;

import ast.*;
import ast.ClassDeclaration.ClassDeclExtends;
import ast.ClassDeclaration.ClassDeclSimple;
import ast.expression.*;
import ast.statement.*;
import ast.type.BooleanType;
import ast.type.IdentifierType;
import ast.type.IntArrayType;
import ast.type.IntegerType;
import symbol.Table;

public class SymbolVisitor implements Visitor {
	Table<Decl> t = new Table();

	@Override
	public Object visit(Program n) {
		// add main's classname to table
		t.put(n.m.i1.s, n.m);
		// assign main as the binding to it's ids
		n.m.i1.b = n.m;

		// add each class to the table
		n.cl.list.forEach(c -> {
			if(t.contains(c.i.s)); //error
			t.put(c.i.s, c);
		});
		// visit the main
		n.m.accept(this);
		// visit each class
		n.cl.list.forEach(c -> c.accept(this));

		return null;
	}

	@Override
	public Object visit(MainClass n) {
		t.beginScope();
		if(t.contains(n.i2.s)); //error
		t.put(n.i2.s, n);
		n.s.accept(this);
		t.endScope();
		return null;
	}

	@Override
	public Object visit(ClassDeclSimple n) {



		t.beginScope();
		// add every variable to the table
		n.getVars().list.forEach(v -> t.put(v.i.s, v));
		// add every method to the table
		n.getMethods().list.forEach(m -> t.put(m.i.s, m));
		t.endScope();
		return null;
	}

	@Override
	public Object visit(ClassDeclExtends n) {
		t.beginScope();
		//TODO check for cyclical inheritance

		// add every variable to the table
		n.getVars().list.forEach(v -> t.put(v.i.s, v));
		// add every method to the table
		n.getMethods().list.forEach(m -> {
			if(t.contains(m.i.s) && differentArgs(t.get(m.i.s), m)); //error
			t.put(m.i.s, m);
		});
		t.endScope();
		return null;
	}

	@Override
	public void visit(VarDecl n) {
		if(t.contains(n.i.s)); //error
		t.put(n.i.s, n);
		return null;
	}

	@Override
	public Object visit(MethodDecl n) {
		t.beginScope();
		if(t.contains(n.i.s)); //error
		t.put()

		t.endScope();
		return null;
	}

	@Override
	public Object visit(Formal n) {
		return null;
	}

	@Override
	public Object visit(IntArrayType n) {
		return null;
	}

	@Override
	public Object visit(BooleanType n) {
		return null;
	}

	@Override
	public Object visit(IntegerType n) {
		return null;
	}

	@Override
	public Object visit(IdentifierType n) {
		return null;
	}

	@Override
	public Object visit(Block n) {
		return null;
	}

	@Override
	public Object visit(If n) {
		return null;
	}

	@Override
	public Object visit(While n) {
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
	public Object visit(ArrayAssign n) {
		return null;
	}

	@Override
	public Object visit(And n) {
		return null;
	}

	@Override
	public Object visit(LessThan n) {
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
	public Object visit(ArrayLookup n) {
		return null;
	}

	@Override
	public Object visit(ArrayLength n) {
		return null;
	}

	@Override
	public Object visit(Call n) {
		return null;
	}

	@Override
	public Object visit(IntegerLiteral n) {
		return null;
	}

	@Override
	public Object visit(True n) {
		return null;
	}

	@Override
	public Object visit(False n) {
		return null;
	}

	@Override
	public Object visit(IdentifierExp n) {
		return null;
	}

	@Override
	public Object visit(This n) {
		return null;
	}

	@Override
	public Object visit(NewArray n) {
		return null;
	}

	@Override
	public Object visit(NewObject n) {
		return null;
	}

	@Override
	public Object visit(Not n) {
		return null;
	}

	@Override
	public Object visit(Identifier n) {
		return null;
	}
}
