import ast.ClassDeclaration.ClassDecl;
import ast.ClassDeclaration.ClassDeclExtends;
import ast.ClassDeclaration.ClassDeclSimple;
import ast.*;
import ast.expression.*;
import ast.statement.*;
import ast.type.*;
import symbol.SymbolTable;

import java.util.ArrayList;

public class SymbolGenerator implements Visitor {

	private SymbolTable<Decl> t = new SymbolTable();
	private ArrayList<ClassDecl> inheritanceChain = new ArrayList<>();

	private void error(Identifier id, String msg) { System.out.println(id.position+  " error: " + msg);}

	public void visit(Program n) {
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
	}

	public void visit(MainClass n) {
		// set the bindings
		n.i.b = n;
		n.i2.b = n;
		// visit the main statement
		n.s.accept(this);
	}

	public void visit(ClassDeclSimple n) {
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
	}

	public void visit(ClassDeclExtends n) {
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
	}

	public void visit(VarDecl n) {
		// just add the decl to whatever the current scope is
		if (t.put(n.i, n) != null) ; //error - var already defined in current scope
	}

	public void visit(MethodDecl n){
		// visit each paramDecl
		n.fl.list.forEach(f -> f.accept(this));
		// visit each varDecl
		n.vl.list.forEach(v -> v.accept(this));
		// visit each statement
		n.sl.list.forEach(s -> s.accept(this));
	}

	public void visit(Formal n) {
		// just add the decl to whatever the current scope is
		if (t.put(n.i, n) != null) ; //error - formal already defined in current method
	}

	public void visit(IntArrayType n) {}

	public void visit(BooleanType n) {}

	public void visit(IntegerType n) {}

	@Override
	public void visit(StringType n) {}

	public void visit(IdentifierType n) {
		n.i.accept(this);
	}

	public void visit(Block n) {
		n.sl.list.forEach(s -> s.accept(this));
	}

	public void visit(If n) {
		n.e.accept(this);
		n.s1.accept(this);
		n.s2.accept(this);
	}

	public void visit(While n) {
		n.e.accept(this);
		n.s.accept(this);
	}

	public void visit(Print n) {
		n.e.accept(this);
	}

	public void visit(Assign n) {
		n.i.accept(this);
		n.e.accept(this);
	}

	public void visit(ArrayAssign n) {
		n.i.accept(this);
		n.e1.accept(this);
		n.e2.accept(this);
	}

	public void visit(And n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}

	public void visit(Or n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}

	public void visit(LessThan n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}

	public void visit(Equals n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}

	public void visit(Plus n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}

	public void visit(Minus n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}

	public void visit(Times n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}

	public void visit(Divide n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}

	public void visit(ArrayLookup n) {
		n.e1.accept(this);
		n.e2.accept(this);
	}

	public void visit(ArrayLength n) {
		n.e.accept(this);
	}

	public void visit(Call n) {
		n.e.accept(this);
		// do not visit the identifier because it is out of scope //n.i.accept(this)
		n.el.list.forEach(e -> e.accept(this));
	}

	public void visit(IntegerLiteral n) {}

	@Override
	public void visit(StringLiteral n) {}

	public void visit(True n) {}

	public void visit(False n) {}

	public void visit(IdentifierExp n) {
		n.i.accept(this);
	}

	public void visit(This n) {
		if(inheritanceChain.isEmpty()); //error - reference from inside main method
	}

	public void visit(NewArray n) { n.e.accept(this); }

	public void visit(NewObject n) { n.i.accept(this); }

	public void visit(Not n) { n.e.accept(this);}

	public void visit(Identifier n) {
		// assign this identifier to its deceleration
		n = t.get(n).i;
	}
}
