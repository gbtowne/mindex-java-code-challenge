package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.resource.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String reportsIdUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        reportsIdUrl = "http://localhost:" + port + "/reports/{id}";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    @Test
    public void testReports() {
        // test reporting structure for employee with 0 reports
        String paulMcCartneyId = "b7839309-3348-463b-a7e3-5de1c168beb3";
        ReportingStructure paulMcCartneyReportingStructure = restTemplate.getForEntity(reportsIdUrl, ReportingStructure.class, paulMcCartneyId).getBody();

        assertNotNull(paulMcCartneyReportingStructure);
        assertEquals(paulMcCartneyReportingStructure.getEmployee().getEmployeeId(), paulMcCartneyId);
        assertEquals(paulMcCartneyReportingStructure.getNumberOfReports(), 0);

        // test reporting structure for employee with 2 direct reports
        String ringoStarrId = "03aa1462-ffa9-4978-901b-7c001562cf6f";
        ReportingStructure ringoStarrReportingStructure = restTemplate.getForEntity(reportsIdUrl, ReportingStructure.class, ringoStarrId).getBody();

        assertNotNull(ringoStarrReportingStructure);
        assertEquals(ringoStarrReportingStructure.getEmployee().getEmployeeId(), ringoStarrId);
        assertEquals(ringoStarrReportingStructure.getNumberOfReports(), 2);

        // test reporting structure for employee with 4 reports
        String johnLennonId = "16a596ae-edd3-4847-99fe-c4518e82c86f";
        ReportingStructure johnLennonReportingStructure = restTemplate.getForEntity(reportsIdUrl, ReportingStructure.class, johnLennonId).getBody();

        assertNotNull(johnLennonReportingStructure);
        assertEquals(johnLennonReportingStructure.getEmployee().getEmployeeId(), johnLennonId);
        assertEquals(johnLennonReportingStructure.getNumberOfReports(), 4);

    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
