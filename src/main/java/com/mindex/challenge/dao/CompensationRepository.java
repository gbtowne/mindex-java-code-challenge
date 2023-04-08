package com.mindex.challenge.dao;

import com.mindex.challenge.data.Compensation;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface CompensationRepository extends MongoRepository<Compensation, String> {
    Compensation findByEmployeeEmployeeId(String employeeId);
}
