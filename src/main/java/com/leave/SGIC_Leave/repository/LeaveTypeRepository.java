package com.leave.SGIC_Leave.repository;

import com.leave.SGIC_Leave.model.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long> {
}
