package com.leave.SGIC_Leave.service;
import com.leave.SGIC_Leave.exception.FileUploadHandler;
import com.leave.SGIC_Leave.model.Employee;
import com.leave.SGIC_Leave.model.LeaveBalance;
import com.leave.SGIC_Leave.model.LeaveType;
import com.leave.SGIC_Leave.repository.EmployeeRepository;
import com.leave.SGIC_Leave.repository.LeaveBalanceRepository;
import com.leave.SGIC_Leave.repository.LeaveTypeRepository;
import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService{

    private final EmployeeRepository employeeRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveTypeRepository leaveTypeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository,
                           LeaveBalanceRepository leaveBalanceRepository,
                           LeaveTypeRepository leaveTypeRepository) {
        this.employeeRepository = employeeRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.leaveTypeRepository = leaveTypeRepository;
    }


    public Employee addEmployee(Employee employee) {

        Employee savedEmployee = employeeRepository.save(employee);


        List<LeaveType> leaveTypes = leaveTypeRepository.findAll();
        for (LeaveType leaveType : leaveTypes) {
            LeaveBalance leaveBalance = new LeaveBalance();
            leaveBalance.setEmployee(savedEmployee);
            leaveBalance.setLeaveType(leaveType);


            if (employee.getHireDate() != null &&
                    Period.between(employee.getHireDate(), LocalDate.now()).getYears() >= 1) {
                leaveBalance.setBalance(leaveType.getYearLeave());
            } else {
                leaveBalance.setBalance(0);
            }

            leaveBalanceRepository.save(leaveBalance);
        }

        return savedEmployee;

    }
    public Employee findById(Long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        return employeeOptional.orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    // file upload
    public void importFile(MultipartFile file){
        try {
            InputStream inputStream = file.getInputStream();
            CsvParserSettings settings = new CsvParserSettings();
            settings.setHeaderExtractionEnabled(true);
            CsvParser csvParser = new CsvParser(settings);
            List<Record> records = csvParser.parseAllRecords(inputStream);

            List<Employee> employees = new ArrayList<>();
            for (Record record : records) {
                Employee employee = new Employee();
                employee.setFirstName(record.getString("firstname"));
                employee.setLastName(record.getString("lastName"));
                employee.setEmail(record.getString("email"));
                employee.setPhoneNumber(record.getString("phone_number"));
                employee.setPosition(record.getString("position"));
                employee.setHireDate(LocalDate.parse(record.getString("hireDate")));
                employees.add(employeeRepository.save(employee));
            }
//        } catch (IOException e) {
//            throw new FileNotFoundException("File not found or cannot be processed: " + e.getMessage());
        }
        catch (Exception e) {
            throw new FileUploadHandler("Error while processing file: " + e.getMessage());
        }
    }
}
