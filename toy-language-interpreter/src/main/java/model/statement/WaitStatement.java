package model.statement;

import adt.IDictionary;
import adt.IStack;
import exception.ExpressionException;
import exception.StatementException;
import model.ProgramState;
import model.expression.Expression;
import model.expression.ValueExpression;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.Value;

public class WaitStatement implements Statement {
    private final Expression number;

    public WaitStatement(Expression number) {
        this.number = number;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException, ExpressionException {
        IStack<Statement> executionStack = state.getExecutionStack();
        IDictionary<String, Value> symbolTable = state.getSymbolTable();

        Value value = number.eval(symbolTable, state.getHeap());
        if (!(value.getType() instanceof IntType)) {
            throw new StatementException("WaitStatement: The expression must evaluate to an integer.");
        }

        int numberValue = ((IntValue) value).getValue();

        if (numberValue == 0) {
            // Do nothing if number is 0
            return null;
        }

        // Push (print(number); wait(number - 1)) onto the stack
        Statement printStatement = new PrintStatement(number);
        Statement recursiveWait = new WaitStatement(new ValueExpression(new IntValue(numberValue - 1)));
        executionStack.push(new CompoundStatement(printStatement, recursiveWait));

        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnvironment) throws StatementException, ExpressionException {
        Type type = number.typeCheck(typeEnvironment);
        if (!type.equals(new IntType())) {
            throw new StatementException("WaitStatement: The expression must evaluate to an integer.");
        }
        return typeEnvironment;
    }

    @Override
    public Statement deepCopy() {
        return new WaitStatement(number.deepCopy());
    }

    @Override
    public String toString() {
        return "wait(" + number.toString() + ")";
    }
}
