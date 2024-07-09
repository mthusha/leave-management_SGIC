package com.leave.SGIC_Leave.service;
import com.leave.SGIC_Leave.CONST.Status;
import com.leave.SGIC_Leave.exception.EmployeeNotFoundException;
import com.leave.SGIC_Leave.model.Employee;
import com.leave.SGIC_Leave.model.LeaveBalance;
import com.leave.SGIC_Leave.model.LeaveRequest;
import com.leave.SGIC_Leave.model.LeaveType;
import com.leave.SGIC_Leave.repository.EmployeeRepository;
import com.leave.SGIC_Leave.repository.LeaveBalanceRepository;
import com.leave.SGIC_Leave.repository.LeaveRequestRepository;
import com.leave.SGIC_Leave.repository.LeaveTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.leave.SGIC_Leave.exception.InsufficientLeaveBalanceException;
import java.util.Date;
import java.util.List;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    @Autowired
    public LeaveRequestService(LeaveBalanceRepository leaveBalanceRepository, LeaveRequestRepository leaveRequestRepository, EmployeeRepository employeeRepository, LeaveTypeRepository leaveTypeRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
    }

    public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest) {
        Employee employee = employeeRepository.findById(leaveRequest.getEmployee().getId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        LeaveType leaveType = leaveTypeRepository.findById(leaveRequest.getLeaveType().getId())
                .orElseThrow(() -> new RuntimeException("LeaveType not found"));

        LeaveBalance leaveBalance = leaveBalanceRepository.findByEmployeeIdAndLeaveTypeId(employee.getId(), leaveType.getId())
                .orElseThrow(() -> new RuntimeException("Leave balance not found for the employee and leave type"));

        if (leaveBalance.getBalance() <= 0) {
            throw new InsufficientLeaveBalanceException("Insufficient leave balance");
        }

        leaveRequest.setEmployee(employee);
        leaveRequest.setLeaveType(leaveType);
        leaveRequest.setStatus(Status.PENDING);

        return leaveRequestRepository.save(leaveRequest);
    }


    public List<LeaveRequest> getAllPendingLeaveRequests() {
        return leaveRequestRepository.findByStatus(Status.PENDING);
    }


    @Transactional
    public LeaveRequest updateLeaveRequestStatus(Long id, Status status) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LeaveRequest not found"));

        if (status == Status.APPROVED || status == Status.REJECTED) {
            leaveRequest.setStatus(status);
            leaveRequest.setUpdatedAt(new Date());

            if (status == Status.APPROVED) {
                deductLeaveBalance(leaveRequest);
            }

            return leaveRequestRepository.save(leaveRequest);
        } else {
            throw new IllegalArgumentException("Invalid status value");
        }
    }

    private void deductLeaveBalance(LeaveRequest leaveRequest) {
        Long employeeId = leaveRequest.getEmployee().getId();
        Long leaveTypeId = leaveRequest.getLeaveType().getId();

        LeaveBalance leaveBalance = leaveBalanceRepository.findByEmployeeIdAndLeaveTypeId(employeeId, leaveTypeId)
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));

        leaveBalance.deduct();

        leaveBalanceRepository.save(leaveBalance);
    }


    public List<LeaveRequest> getAllLeaveRequestsByEmployeeId(Long employeeId) {
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findByEmployeeId(employeeId);
        if (leaveRequests.isEmpty()) {
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " not found.");
        }
        return leaveRequests;
    }


}
