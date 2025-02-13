package model.statement;

import adt.IDictionary;
import exception.ExpressionException;
import exception.StatementException;
import model.ProgramState;
import model.type.Type;

public interface Statement {
    ProgramState execute(ProgramState state) throws StatementException, ExpressionException;

    IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnvironment) throws StatementException, ExpressionException;

    Statement deepCopy();
}
