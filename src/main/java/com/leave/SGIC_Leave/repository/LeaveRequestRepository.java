package com.leave.SGIC_Leave.repository;

import com.leave.SGIC_Leave.CONST.Status;
import com.leave.SGIC_Leave.model.Employee;
import com.leave.SGIC_Leave.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByStatus(Status status);
    List<LeaveRequest> findByEmployeeId(Long employeeId);
}
