public class toDNF {
	abstract class Expr {
		abstract Expr toDNF();
	}
	
	class Literal extends Expr {
		String name;
	
		Literal(String name) {
			this.name = name;
		}
	
		@Override
		Expr toDNF() {
			return this;
		}
	
		@Override
		public String toString() {
			return name;
		}
	}
	
	class Not extends Expr {
		Expr expr;
	
		Not(Expr expr) {
			this.expr = expr;
		}
	
		@Override
		Expr toDNF() {
			if (expr instanceof Literal) {
				return this; // NOT applied to a literal is already in DNF
			} else if (expr instanceof Not) {
				// Double negation elimination: NOT(NOT(A)) -> A
				return ((Not) expr).expr.toDNF();
			} else if (expr instanceof And) {
				// De Morgan's Law: NOT(A AND B) -> NOT(A) OR NOT(B)
				And and = (And) expr;
				return new Or(new Not(and.left).toDNF(), new Not(and.right).toDNF());
			} else if (expr instanceof Or) {
				// De Morgan's Law: NOT(A OR B) -> NOT(A) AND NOT(B)
				Or or = (Or) expr;
				return new And(new Not(or.left).toDNF(), new Not(or.right).toDNF());
			}
			return this;
		}
	
		@Override
		public String toString() {
			return "NOT(" + expr + ")";
		}
	}
	
	class And extends Expr {
		Expr left, right;
	
		And(Expr left, Expr right) {
			this.left = left;
			this.right = right;
		}
	
		@Override
		Expr toDNF() {
			return new And(left.toDNF(), right.toDNF()); // DNF propagation
		}
	
		@Override
		public String toString() {
			return "(" + left + " AND " + right + ")";
		}
	}
	
	class Or extends Expr {
		Expr left, right;
	
		Or(Expr left, Expr right) {
			this.left = left;
			this.right = right;
		}
	
		@Override
		Expr toDNF() {
			Expr leftDNF = left.toDNF();
			Expr rightDNF = right.toDNF();
	
			// Distribute AND over OR: (A AND B) OR C -> (A OR C) AND (B OR C)
			if (leftDNF instanceof And) {
				And andLeft = (And) leftDNF;
				return new And(new Or(andLeft.left, rightDNF).toDNF(), new Or(andLeft.right, rightDNF).toDNF());
			} else if (rightDNF instanceof And) {
				And andRight = (And) rightDNF;
				return new And(new Or(leftDNF, andRight.left).toDNF(), new Or(leftDNF, andRight.right).toDNF());
			}
			return new Or(leftDNF, rightDNF);
		}
	
		@Override
		public String toString() {
			return "(" + left + " OR " + right + ")";
		}
	}
	
}
