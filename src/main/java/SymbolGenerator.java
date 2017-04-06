import ast.ClassDeclaration.ClassDecl;
import ast.ClassDeclaration.ClassDeclExtends;
import ast.ClassDeclaration.ClassDeclSimple;
import ast.*;
import ast.expression.*;
import ast.statement.*;
import ast.type.BooleanType;
import ast.type.IdentifierType;
import ast.type.IntArrayType;
import ast.type.IntegerType;
import symbol.SymbolTable;

import java.util.ArrayList;

public class SymbolGenerator implements Visitor {

	private SymbolTable<Decl> t = new SymbolTable();
	private ArrayList<ClassDecl> inheritanceChain = new ArrayList<>();

	public Object visit(Program n) {
		// add the mainDecl to global scope
		t.put(n.m.i, n.m);
		// add all classDecl to global scope
		n.cl.list.forEach(c -> t.put(c.i, c));
		// visit the mainDecl
		n.m.accept(this);
		// visit each class
		n.cl.list.forEach(c -> {
			inheritanceChain.clear();
			t.beginScope();
			c.accept(this);
			t.endScope();
		});
		return null;
	}

	public Object visit(MainClass n) {
		// set the bindings
		n.i.b = n;
		n.i2.b = n;
		// visit the main statement
		n.s.accept(this);
		return null;
	}

	public Object visit(ClassDeclSimple n) {
		// add n to the inheritance chain just so to handle "this"
		inheritanceChain.add(n);
		// assign class identifier to it's decl
		n.i.b = n;
		// add each varDecl to class scope
		n.vl.list.forEach(v -> v.accept(this));
		// add each methodDecl to class scope (because methods can reference each other)
		n.ml.list.forEach(m -> t.put(m.i, m));
		// visit each methodDecl
		n.ml.list.forEach(m -> {
			t.beginScope();
			m.accept(this);
			t.endScope();
		});
		return null;
	}

	public Object visit(ClassDeclExtends n) {
		// check for cyclical inheritance
		if(inheritanceChain.contains(n)); //error - cyclical inheritance
		else inheritanceChain.add(n);
		// add all parent varDecl and methodDecl to current scope
		t.get(n.parent).accept(this);
		// add all varDecl for current class
		n.vl.list.forEach(v -> v.accept(this));
		// add each methodDecl to class scope (because methods can reference each other)
		n.ml.list.forEach(m -> {
			MethodDecl last = (MethodDecl) t.put(m.i, m);
			if (last != null && last.fl.list.size() != m.fl.list.size()); //error - method override with different args
		});
		// visit each methodDecl
		n.ml.list.forEach(m -> {
			t.beginScope();
			m.accept(this);
			t.endScope();
		});
		return null;
	}

	public Object visit(VarDecl n) {
		// just add the decl to whatever the current scope is
		if (t.put(n.i, n) != null) ; //error - var already defined in current scope
		return null;
	}

	public Object visit(MethodDecl n) {
		// visit each paramDecl
		n.fl.list.forEach(f -> f.accept(this));
		// visit each varDecl
		n.vl.list.forEach(v -> v.accept(this));
		// visit each statement
		n.sl.list.forEach(s -> s.accept(this));
		return null;
	}

	public Object visit(Formal n) {
		// just add the decl to whatever the current scope is
		if (t.put(n.i, n) != null) ; //error - formal already defined in current method
		return null;
	}

	public Object visit(IntArrayType n) {
		return null;
	}

	public Object visit(BooleanType n) {
		return null;
	}

	public Object visit(IntegerType n) {
		return null;
	}

	public Object visit(IdentifierType n) {
		return null;
	}

	public Object visit(Block n) {
		n.sl.list.forEach(s -> s.accept(this));
		return null;
	}

	public Object visit(If n) {
		n.e.accept(this);
		n.s1.accept(this);
		n.s2.accept(this);
		return null;
	}

	public Object visit(While n) {
		n.e.accept(this);
		n.s.accept(this);
		return null;
	}

	public Object visit(Print n) {
		n.e.accept(this);
		return null;
	}

	public Object visit(Assign n) {
		n.i.accept(this);
		n.e.accept(this);
		return null;
	}

	public Object visit(ArrayAssign n) {
		n.i.accept(this);
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	public Object visit(And n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	public Object visit(LessThan n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	public Object visit(Plus n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	public Object visit(Minus n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	public Object visit(Times n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	public Object visit(ArrayLookup n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	public Object visit(ArrayLength n) {
		n.e.accept(this);
		return null;
	}

	public Object visit(Call n) {
		n.e.accept(this);
		// do not visit the identifier because it is out of scope //n.i.accept(this)
		n.el.list.forEach(e -> e.accept(this));
		return null;
	}

	public Object visit(IntegerLiteral n) {
		return null;
	}

	public Object visit(True n) {
		return null;
	}

	public Object visit(False n) {
		return null;
	}

	@Override //TODO : should IdentifierExp be a subclass of Identifier?
	public Object visit(IdentifierExp n) {
		return null;
	}

	public Object visit(This n) {
		if(inheritanceChain.isEmpty()); //error - reference from inside main method
		return null;
	}

	public Object visit(NewArray n) {
		n.e.accept(this);
		return null;
	}

	public Object visit(NewObject n) {
		n.i.accept(this);
		return null;
	}

	public Object visit(Not n) {
		n.e.accept(this);
		return null;
	}

	public Object visit(Identifier n) {
		// assign this identifier to its deceleration
		n = t.get(n).i;
		return null;
	}
}
