package com.leave.SGIC_Leave.controller;
import com.leave.SGIC_Leave.CONST.Const;
import com.leave.SGIC_Leave.exception.FileUploadHandler;
import com.leave.SGIC_Leave.model.Employee;
import com.leave.SGIC_Leave.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(Const.ADD_EMPLOYEE_URL)
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        Employee newEmployee = employeeService.addEmployee(employee);
        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }

    @PostMapping(Const.FILE_UPLOAD)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            employeeService.importFile(file);
            return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
        } catch (Exception e) {
            throw new FileUploadHandler("Failed to upload file: " + e.getMessage());
        }
    }

}
