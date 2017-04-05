import oldast.Binding;
import oldast.ID;
import oldast.TypeIdList;
import oldast.Visitor;
import oldast.expression.*;
import oldast.expression.operators.*;
import oldast.statement.*;
import oldast.type.Boolean;
import oldast.type.Int;
import oldast.type.IntArray;
import oldast.type.String;
import symbol.Symbol;

import java.util.HashMap;

public class SymbolGenerator implements Visitor {
	HashMap<Symbol, Binding> global, curClass, curMethod;

	@Override
	public Object visit(TypeIdList n) {
		return null;
	}

	@Override
	public Object visit(Program n) {
		global = new HashMap<>();
		global.put(Symbol.symbol(n.main.id.id), n.main);
		n.classDeclarations.forEach(c -> global.put(Symbol.symbol(c.id.id), c));
		n.main.accept(this);
		n.classDeclarations.forEach(c -> {
			curClass = new HashMap<>();
			c.accept(this);
		});
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
		if(n.parent != null) n.a
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
		global.put(Symbol.symbol(n.id.id), n);
		curMethod.clear(); curMethod.put(Symbol.symbol(n.args.id)):
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
