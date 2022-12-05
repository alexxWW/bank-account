package io.alex.repository;

import io.alex.model.Operation;

import java.util.List;
import java.util.Optional;

public interface OperationsRepository {
    List<Operation> getAll();
    Operation register(Operation operation);
    Optional<Operation> findOperationByOrderByDate();
}
