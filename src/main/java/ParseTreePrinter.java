import ast.Statement.*;
import ast.Type.Array;
import ast.Type.Boolean;
import ast.Type.Class;
import ast.Type.Int;
import ast.TypeIdList;
import ast.Visitor;
import ast.expression.*;

public class ParseTreePrinter implements Visitor<String> {
	@Override
	public String visit(TypeIdList n) {
		StringBuilder b = new StringBuilder("(TY-ID-LIST ");
		for (int i = 0; i < n.types.size(); i++) {
			b.append(" (").append(n.types.get(i).accept(this)).append(" ").append(n.ids.get(i).accept(this)).append
					(")");
		}
		return b.append(")").toString();
	}

	@Override
	public String visit(Program n) {
		return null;
	}

	@Override
	public String visit(Print n) {
		return null;
	}

	@Override
	public String visit(Assign n) {
		return null;
	}

	@Override
	public String visit(Skip n) {
		return null;
	}

	@Override
	public String visit(Block n) {
		StringBuilder b = new StringBuilder("(BLOCK\n");
		for (Statement s : n.statements) {
			b.append(s.accept(this)).append("\n");
		}
		return b.append(")\n").toString();
	}

	@Override
	public String visit(IfThenElse n) {
		return "(IF " + n.expr.accept(this) + ")\n\t" + n.then.accept(this) + "\n\t" + n.elze.accept(this) + "\n)";
	}

	@Override
	public String visit(While n) {
		return null;
	}

	@Override
	public String visit(Sidef n) {
		return null;
	}

	@Override
	public String visit(ClassDeclaration n) {
		return null;
	}

	@Override
	public String visit(VarDeclaration n) {
		return "(VAR-DECL " + n.type.accept(this) + " " + n.id.accept(this) + ")";
	}

	@Override
	public String visit(MethodDeclaration n) {
		return "(MTD-DECL " + n.returnType.accept(this) + " " + n.id.accept(this) + " " + n.params.accept(this) + "\n";
	}

	@Override
	public String visit(MainClassDeclaration n) {
		return null;
	}

	@Override
	public String visit(Return n) {
		return "(RETURN " + n.returnValue.accept(this) + ")";
	}

	@Override
	public String visit(Int n) {
		return null;
	}

	@Override
	public String visit(Boolean n) {
		return null;
	}

	@Override
	public String visit(ast.Type.String n) {
		return null;
	}

	@Override
	public String visit(Array n) {
		return null;
	}

	@Override
	public String visit(Class n) {
		return null;
	}

	@Override
	public String visit(Method n) {
		return null;
	}

	@Override
	public String visit(Var n) {
		return null;
	}

	@Override
	public String visit(IntLiteral n) {
		return null;
	}

	@Override
	public String visit(StringLiteral n) {
		return null;
	}

	@Override
	public String visit(StringPlus n) {
		return null;
	}

	@Override
	public String visit(IntPlus n) {
		return null;
	}

	@Override
	public String visit(Minus n) {
		return null;
	}

	@Override
	public String visit(Times n) {
		return null;
	}

	@Override
	public String visit(Division n) {
		return null;
	}

	@Override
	public String visit(Modulo n) {
		return null;
	}

	@Override
	public String visit(Equals n) {
		return "(EQSIGN " + n.lhs.accept(this) + " " + n.rhs.accept(this) + ")";
	}

	@Override
	public String visit(LessThan n) {
		return "(< " + n.lhs.accept(this) + " " + n.rhs.accept(this);
	}

	@Override
	public String visit(And n) {
		return null;
	}

	@Override
	public String visit(Or n) {
		return null;
	}

	@Override
	public String visit(Neg n) {
		return null;
	}

	@Override
	public String visit(Not n) {
		return null;
	}

	@Override
	public String visit(ID n) {
		return null;
	}

	@Override
	public String visit(FunctionCall n) {
		return null;
	}
}
