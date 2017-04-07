import ast.ClassDeclaration.ClassDecl;
import ast.ClassDeclaration.ClassDeclExtends;
import ast.ClassDeclaration.ClassDeclSimple;
import ast.*;
import ast.expression.*;
import ast.statement.*;
import ast.type.*;
import symbol.SymbolTable;

import java.util.HashSet;
import java.util.TreeSet;

public class SymbolGenerator implements Visitor {

	private SymbolTable<Decl> t = new SymbolTable();
	private HashSet<ClassDecl> inheritanceChain = new HashSet<>();

	private TreeSet<String> errors = new TreeSet<>((s1, s2) -> {
		String[] s1loc = s1.substring(0, s1.indexOf(" ")).split(":");
		String[] s2loc = s2.substring(0, s2.indexOf(" ")).split(":");
		if(Integer.parseInt(s1loc[0]) < Integer.parseInt(s2loc[0])) {
			return -1;
		} else if(Integer.parseInt(s1loc[0]) > Integer.parseInt(s2loc[0])) {
			return 1;
		} else if(Integer.parseInt(s1loc[1]) < Integer.parseInt(s2loc[1])) {
			return -1;
		} else if(Integer.parseInt(s1loc[1]) > Integer.parseInt(s2loc[1])) {
			return 1;
		} else return 0;
	});
	private void error(String loc, String msg) { errors.add(loc +  " error: " + msg); }

	public void visit(Program n) {
		// add the mainDecl to global scope
		t.put(n.m.i, n.m);
		// add all classDecl to global scope
		n.cl.list.forEach(c -> {
			if(t.put(c.i, c) != null) error(c.i.pos, "class is defined more than once");
		});
		// visit the mainDecl
		n.m.accept(this);
		// visit each class
		n.cl.list.forEach(c -> {
			inheritanceChain.clear();
			t.beginScope();
			c.accept(this);
			t.endScope();
		});
		errors.forEach(System.out::println);
	}

	public void visit(MainClass n) {
		// set the bindings
		n.i.b = n;
		n.i2.b = new Formal(new StringType(), n.i2);
		// visit the main statement
		n.s.accept(this);
	}

	public void visit(ClassDeclSimple n) {
		// add n to the inheritance chain just so to handle "this"
		inheritanceChain.add(n);
		// Set the binding for this classDecl
		n.i.b = t.get(n.i);
		// add each varDecl to class scope
		n.vl.list.forEach(v -> {
			if(t.put(v.i, v) != null) error(v.i.pos, "Variable declared multiple times in the same scope");
			v.i.b = v;
		});
		// add each methodDecl to class scope (because methods can reference each other)
		n.ml.list.forEach(m -> {
			if(t.put(m.i, m) != null) error(m.i.pos, "Method declared multiple times in same the scope");
			m.i.b = m;
		});
		// visit each methodDecl
		n.ml.list.forEach(m -> {
			t.beginScope();
			m.accept(this);
			t.endScope();
		});
	}

	public void visit(ClassDeclExtends n) {
		// check for cyclical inheritance
		// add all parent varDecl and methodDecl to current scope
		if(!inheritanceChain.add(n)) error(n.i.pos, "cyclical inheritance");
		else t.get(n.parent).accept(this);
		// Set the binding for this classDecl and it's parent
		n.i.b = t.get(n.i); n.parent.b = t.get(n.parent);
		// add all varDecl for current class
		n.vl.list.forEach(v -> v.accept(this));
		// add each methodDecl to class scope (because methods can reference each other)
		n.ml.list.forEach(m -> {
			MethodDecl last = (MethodDecl) t.get(m.i);
			if (last != null && last.fl.list.size() != m.fl.list.size()) error(m.i.pos, "method override with different args");
			else t.put(m.i, m);
			m.i.b = m;
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
		if(t.put(n.i, n) != null) error(n.i.pos, "var already defined in current scope");
		n.i.b = n;
	}

	public void visit(MethodDecl n){
		// visit each paramDecl
		n.fl.list.forEach(f -> f.accept(this));
		// visit each varDecl
		n.vl.list.forEach(v -> v.accept(this));
		// visit each statement
		n.sl.list.forEach(s -> s.accept(this));
		n.e.accept(this);
	}

	public void visit(Formal n) {
		// just add the decl to whatever the current scope is
		if (t.put(n.i, n) != null) error(n.i.pos, "formal already defined in current method");
		n.i.b = n;
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
		if(n.s2 != null) n.s2.accept(this);
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
		if(inheritanceChain.isEmpty()) error(n.pos,  "reference this from inside main method");
	}

	public void visit(NewArray n) { n.e.accept(this); }

	public void visit(NewObject n) { n.i.accept(this); }

	public void visit(Not n) { n.e.accept(this);}

	public void visit(Identifier n) {
		// assign this identifier to its deceleration
		Decl d = t.get(n);
		if(d == null) error(n.pos, "identifier not found in symbol table");
		else n.b = d;
	}
}
