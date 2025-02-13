package model.statement.semaphore;

import adt.*;
import exception.ExpressionException;
import exception.StatementException;
import model.ProgramState;
import model.expression.Expression;
import model.statement.Statement;
import model.type.IntType;
import model.type.Type;
import model.value.IntValue;
import model.value.Value;

import java.util.ArrayList;
import java.util.List;

// CountSemaphore
public class CreateSemaphoreStatement implements Statement {
    private final String var;
    private final Expression exp;

    public CreateSemaphoreStatement(String var, Expression exp) {
        this.var = var;
        this.exp = exp;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementException {
        IDictionary<String, Value> symbolTable = state.getSymbolTable();
        IHeap heap = state.getHeap();
        ISyncTable semaphoreTable = state.getSemaphoreTable();

        Value evaluatedExp;
        try {
            evaluatedExp = this.exp.eval(symbolTable, heap);
            if (!evaluatedExp.getType().equals(new IntType())) {
                throw new StatementException("Expression is not of type int!");
            }
        } catch (Exception e) {
            throw new StatementException("Error evaluating expression: " + e.getMessage());
        }

        int semaphoreValue = ((IntValue) evaluatedExp).getValue();
        if (!symbolTable.search(this.var) || !symbolTable.get(this.var).getType().equals(new IntType())) {
            throw new StatementException("Variable " + this.var + " is not defined or not of type int!");
        }

        int address = semaphoreTable.add(new Pair<>(semaphoreValue, new ArrayList<>()));
        symbolTable.update(this.var, new IntValue(address));

        return null;
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnvironment) throws StatementException, ExpressionException {
        Type varType = typeEnvironment.get(this.var);
        Type expType = this.exp.typeCheck(typeEnvironment);

        if (!varType.equals(new IntType())) {
            throw new StatementException("Variable " + this.var + " is not of type int!");
        }
        if (!expType.equals(new IntType())) {
            throw new StatementException("Expression is not of type int!");
        }

        return typeEnvironment;
    }

    @Override
    public Statement deepCopy() {
        return new CreateSemaphoreStatement(this.var, this.exp.deepCopy());
    }

    public String toString() {
        return "createSemaphore(" + this.var + ", " + this.exp + ")";
    }
}
