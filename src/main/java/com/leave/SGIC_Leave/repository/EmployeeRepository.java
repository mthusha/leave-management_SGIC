package com.leave.SGIC_Leave.repository;

import com.leave.SGIC_Leave.model.Employee;
import com.leave.SGIC_Leave.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
   // public void importEmployee(MultipartFile file);
}
