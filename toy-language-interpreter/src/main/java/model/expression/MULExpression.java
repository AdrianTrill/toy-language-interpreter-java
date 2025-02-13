package model.expression;

import adt.IDictionary;
import adt.IHeap;
import exception.ExpressionException;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.Value;

public class MULExpression implements Expression {
    private final Expression e1;
    private final Expression e2;

    public MULExpression(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public Value eval(IDictionary<String, Value> symbolTable, IHeap heap) throws ExpressionException {
        Value v1 = e1.eval(symbolTable, heap);
        if (!v1.getType().equals(new IntType())) {
            throw new ExpressionException("First operand is not an integer");
        }
        Value v2 = e2.eval(symbolTable, heap);
        if (!v2.getType().equals(new IntType())) {
            throw new ExpressionException("Second operand is not an integer");
        }

        int n1 = ((IntValue) v1).getValue();
        int n2 = ((IntValue) v2).getValue();

        // Evaluate MUL as ((n1 * n2) - (n1 + n2))
        int result = (n1 * n2) - (n1 + n2);

        return new IntValue(result);
    }

    @Override
    public Type typeCheck(IDictionary<String, Type> typeEnvironment) throws ExpressionException {
        Type type1 = e1.typeCheck(typeEnvironment);
        Type type2 = e2.typeCheck(typeEnvironment);

        if (!type1.equals(new IntType()) || !type2.equals(new IntType())) {
            throw new ExpressionException("Both operands must be integers");
        }

        return new IntType();
    }

    @Override
    public Expression deepCopy() {
        return new MULExpression(this.e1.deepCopy(), this.e2.deepCopy());
    }

    @Override
    public String toString() {
        return "MUL(" + e1 + ", " + e2 + ")";
    }
}
