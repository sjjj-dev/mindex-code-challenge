package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure read(String id) {
        LOG.debug("Creating reporting structure with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid id: " + id);
        }

        ReportingStructure reportingStructure = new ReportingStructure();
        reportingStructure.setEmployee(employee);
        reportingStructure.setNumberOfReports(getNumberOfReports(employee));

        return reportingStructure;
    }

    private int getNumberOfReports(Employee employee){

        int numberOfReports = 0;

        if (employee.getDirectReports() == null){
            return numberOfReports;
        }

        for (Employee directReport : employee.getDirectReports()){
            numberOfReports++;
            //Recursively count grandchild direct reporters of child direct reporters until all reporters are found
            numberOfReports += getNumberOfReports(employeeRepository.findByEmployeeId(directReport.getEmployeeId()));
        }

        return numberOfReports;
    }
}
