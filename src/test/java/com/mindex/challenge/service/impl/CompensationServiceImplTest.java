package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/compensation";
        employeeIdUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void testCreateRead() {
        Compensation testCompensation = new Compensation();
        testCompensation.setEmployeeId("employeeId");
        testCompensation.setSalary(50000);
        testCompensation.setEffectiveDate(LocalDate.of(2024,6,5));

        // Create checks
        Compensation createdCompensation = restTemplate.postForEntity(employeeUrl, testCompensation, Compensation.class).getBody();

        assertNotNull(createdCompensation.getEmployeeId());
        assertCompensationEquivalence(testCompensation, createdCompensation);


        // Read checks
        Compensation readCompensation = restTemplate.getForEntity(employeeIdUrl, Compensation.class, createdCompensation.getEmployeeId()).getBody();
        assertEquals(createdCompensation.getEmployeeId(), readCompensation.getEmployeeId());
        assertCompensationEquivalence(createdCompensation, readCompensation);
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEmployeeId(), actual.getEmployeeId());
        assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }
}
