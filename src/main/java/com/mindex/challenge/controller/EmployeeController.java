package com.mindex.challenge.controller;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.resource.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee")
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return employeeService.create(employee);
    }

    @GetMapping("/employee/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received employee get request for id [{}]", id);

        return employeeService.read(id);
    }

    @PutMapping("/employee/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee update request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }

    @GetMapping("/reports/{id}")
    public ReportingStructure reports(@PathVariable String id) {
        LOG.debug("Received employee report request for id [{}]", id);
        Employee employee = employeeService.read(id);
        int numberOfReports = numberOfReports(id);
        return new ReportingStructure(employee, numberOfReports);
    }

    private Integer numberOfReports(String id) {
        Employee employee = read(id);
        if (CollectionUtils.isEmpty(employee.getDirectReports())) {
            return 0;
        }
        int reportsCount = employee.getDirectReports().size();
        for (Employee report : employee.getDirectReports()) {
            reportsCount += numberOfReports(report.getEmployeeId());
        }
        return reportsCount;
    }
}
