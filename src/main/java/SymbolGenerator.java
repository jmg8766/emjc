import ast.ClassDeclaration.ClassDecl; import ast.ClassDeclaration.ClassDeclExtends;
import ast.ClassDeclaration.ClassDeclSimple;
import ast.*;
import ast.expression.*;
import ast.statement.*;
import ast.type.*;
import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import symbol.SymbolTable;

import java.io.PrintStream;
import java.util.LinkedHashSet;
import java.util.TreeSet;

public class SymbolGenerator implements Visitor<Object> {

	// Default output stream which can be changed by a test when testing the output
	public static PrintStream printStream = System.out;

	private SymbolTable<Decl> t = new SymbolTable();

	// Vars used for error reporting
	private String mainClassName;
	private LinkedHashSet<ClassDecl> inheritanceChain = new LinkedHashSet<>(); //TODO

	private TreeSet<String> errors = new TreeSet<>((s1, s2) -> {
		String[] s1loc = s1.substring(0, s1.indexOf(" ")).split(":");
		String[] s2loc = s2.substring(0, s2.indexOf(" ")).split(":");
		if (Integer.parseInt(s1loc[0]) < Integer.parseInt(s2loc[0])) {
			return -1;
		} else if (Integer.parseInt(s1loc[0]) > Integer.parseInt(s2loc[0])) {
			return 1;
		} else if (Integer.parseInt(s1loc[1]) < Integer.parseInt(s2loc[1])) {
			return -1;
		} else if (Integer.parseInt(s1loc[1]) > Integer.parseInt(s2loc[1])) {
			return 1;
		} else return 0;
	});

	private void error(String loc, String msg) {
		errors.add(loc + " error: " + msg);
	}

	public String visit(Program n) {
		mainClassName = n.m.i.s;
		// add all classDecl to global scope
		n.cl.list.forEach(c -> {
			if (t.put(c.i, c) != null) error(c.i.pos, "class is defined more than once");
			else {c.t = IdentifierType.getInstance(c.i); ((IdentifierType)c.t).decl = c; }});

		// visit the mainDecl
		n.m.accept(this);
		// visit each class
		n.cl.list.forEach(c -> {
			inheritanceChain.clear();
			t.beginScope();
			c.accept(this);
			t.endScope();
			LinkedHashSet ps = new LinkedHashSet();
			ps.addAll(inheritanceChain);
			c.parentSet = ps;
		});

		if (errors.isEmpty()) return "Valid eMiniJava Program";
		else {
		    StringBuilder b = new StringBuilder();
			errors.forEach(e -> b.append(e).append("\n"));
			return b.toString();
		}
	}

	public Object visit(MainClass n) {
		// set the bindings
		n.i.b = n;
		// Set class type
		n.t = IdentifierType.getInstance(n.i);
		n.i2.b = new Formal(StringType.getInstance(), n.i2);
		// visit the main statement
		n.s.accept(this);
		return null;
	}

	public Object visit(ClassDeclSimple n) {
		// add n to the inheritance chain just so to handle "this"
		inheritanceChain.add(n);

		// Set the binding for this classDecl
		n.i.b = t.get(n.i);

//		n.t = IdentifierType.getInstance(n.i);
//		((IdentifierType)n.t).decl = n;

		// add each varDecl to class scope
		n.vl.list.forEach(v -> {
			if (t.put(v.i, v) != null) error(v.i.pos, "Variable declared multiple times in the same scope");
			v.accept(this);
		});

		// add each methodDecl to class scope (because methods can reference each other)
		n.ml.list.forEach(m -> {
			if (t.put(m.i, m) != null) error(m.i.pos, "Method declared multiple times in same the scope");
			m.i.b = m;
		});

		// visit each methodDecl
		n.ml.list.forEach(m -> {
			t.beginScope();
			m.accept(this);
			t.endScope();
		});

		return null;
	}

	public Object visit(ClassDeclExtends n) {
		// check for cyclical inheritance by adding all parent varDecl and methodDecl to current scope
		if(n.parent.s.equals(mainClassName)) error(n.i.pos, "Cannot extend the main class: [" + mainClassName + "]");
		else if (!inheritanceChain.add(n)) error(n.i.pos, "cyclical inheritance");
		else if(t.get(n.parent) == null) error(n.i.pos, "Attempted to extend non-existent class: [" + n.parent.s + "]");
		else t.get(n.parent).accept(this);

		// add information about all supertypes of this class to it's type
		inheritanceChain.forEach(classDecl -> ((IdentifierType)n.t).superTypes.add(classDecl.t));

		// Set the binding for this classDecl and it's parent
		n.i.b = t.get(n.i);

		// set information about the classDecl in the identifier type for this class
//		n.t = IdentifierType.getInstance(n.i);
//		((IdentifierType)n.t).decl = n;

		n.parent.b = t.get(n.parent);

		// add all varDecl for current class
		n.vl.list.forEach(v -> v.accept(this));

		// add each methodDecl to class scope (because methods can reference each other)
		n.ml.list.forEach(m -> {
			Decl last = t.get(m.i);
			// if there is a method with the same name in scope, then a method is being overridden
			if (last != null && last instanceof MethodDecl) {
				// Check for different number of arguments in the overriding method
				if (((MethodDecl) last).fl.list.size() != m.fl.list.size()) {
					error(m.i.pos, "method override with different args");
				}
				// If both the overriding and overridden methods aren't Class types, and they not the same exact type
				if ((!(last.t instanceof IdentifierType) || !(m.t instanceof IdentifierType)) && last.t != m.t) {
					error(m.i.pos, "method override with different type, Expected type: [" + last.t + "] found: ["  + m.t + "]");
				}
				// Otherwise if both are Classes, check if the overriding method is a subtype of the overridden method
				else if(last.t instanceof IdentifierType && m.t instanceof IdentifierType && !((IdentifierType)m.t).superTypes.contains(last.t)) {
                    error(m.i.pos, "method override with different type, Expected subtype: [" + last.t + "] found: ["  + m.t + "]");
                }
			} else t.put(m.i, m);
			m.i.b = m;
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
		if (t.put(n.i, n) != null) error(n.i.pos, "var already defined in current scope");
		n.i.b = n;
		if(n.t instanceof IdentifierType) {
		    Decl d = t.get(((IdentifierType)n.t).i);
			((IdentifierType)n.t).decl = (ClassDecl) d;
		}
		return null;
	}

	public Object visit(MethodDecl n) {
		n.i.b.t = n.t;
		// visit each paramDecl
		n.fl.list.forEach(f -> f.accept(this));
		// visit each varDecl
		n.vl.list.forEach(v -> v.accept(this));
		// visit each statement
		n.sl.list.forEach(s -> s.accept(this));
		n.e.accept(this);
		return null;
	}

	public Object visit(Formal n) {
		// just add the decl to whatever the current scope is
		if (t.put(n.i, n) != null) error(n.i.pos, "formal already defined in current method");
		n.i.b = n;
		if(n.t instanceof IdentifierType) {
			Decl d = t.get(((IdentifierType)n.t).i);
			((IdentifierType)n.t).decl = (ClassDecl) d;
		}
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

	public Object visit(StringType n) {
		return null;
	}

	public Object visit(IdentifierType n) {
		n.i.accept(this);
		return null;
	}

	public Object visit(Block n) {
		n.sl.list.forEach(s -> s.accept(this));
		return null;
	}

	public Object visit(If n) {
		n.e.accept(this);
		n.s1.accept(this);
		if (n.s2 != null) n.s2.accept(this);
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

	public Object visit(Or n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	public Object visit(LessThan n) {
		n.e1.accept(this);
		n.e2.accept(this);
		return null;
	}

	public Object visit(Equals n) {
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

	public Object visit(Divide n) {
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

	public Object visit(StringLiteral n) {
		return null;
	}

	public Object visit(True n) {
		return null;
	}

	public Object visit(False n) {
		return null;
	}

	public Object visit(IdentifierExp n) {
		n.i.accept(this);
		return null;
	}

	public Object visit(This n) {
		if (inheritanceChain.isEmpty()) error(n.pos, "reference this from inside main method");
		n.t = IdentifierType.getInstance(inheritanceChain.iterator().next().i);
		return null;
	}

	public Object visit(NewArray n) {
		n.e.accept(this);
		return null;
	}

	public Object visit(NewObject n) {
		if (t.get(n.i) == null) error(n.i.pos, "Cannot resolve symbol: [" + n.i.s + "]");
		else {
			n.i = t.get(n.i).i;
			n.t = t.get(n.i).t;
		}
		return null;
	}

	public Object visit(Not n) {
		n.e.accept(this);
		return null;
	}

	public Object visit(Identifier n) {
		// assign this identifier to its deceleration
		Decl d = t.get(n);
		if (d == null) error(n.pos, "identifier not found in symbol table");
		else n.b = d;
		return null;
	}
}
