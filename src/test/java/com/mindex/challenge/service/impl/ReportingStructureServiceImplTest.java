package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.DataBootstrap;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.ReportingStructureService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImplTest.class);

    private String employeeIdUrl;

    @Autowired
    private ReportingStructureService reportingStructureService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Before
    public void setup() {
        DataBootstrap bootstrap = new DataBootstrap();
        employeeIdUrl = "http://localhost:" + port + "/reporting-structure/{id}";
    }

    @Test
    public void testRead() {
        Employee testEmployee = employeeRepository.findByEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
        ReportingStructure testReportingStructure = new ReportingStructure();
        testReportingStructure.setEmployee(testEmployee);
        testReportingStructure.setNumberOfReports(4);

        // Read checks
        ReportingStructure readReportingStructure = restTemplate.getForEntity(employeeIdUrl, ReportingStructure.class,"16a596ae-edd3-4847-99fe-c4518e82c86f").getBody();
        assertReportingStructureEquivalence(testReportingStructure, readReportingStructure);
            }

    private static void assertReportingStructureEquivalence(ReportingStructure expected, ReportingStructure actual) {
        assertEquals(expected.getEmployee().getEmployeeId(), actual.getEmployee().getEmployeeId());
        assertEquals(expected.getNumberOfReports(), actual.getNumberOfReports());
    }
}
