package com.leave.SGIC_Leave.repository;
import com.leave.SGIC_Leave.model.Employee;
import com.leave.SGIC_Leave.model.LeaveBalance;
import com.leave.SGIC_Leave.model.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    Optional<LeaveBalance> findByEmployeeIdAndLeaveTypeId(Long employeeId, Long leaveTypeId);
}
