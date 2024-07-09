package com.leave.SGIC_Leave.service;

import com.leave.SGIC_Leave.model.Employee;
import com.leave.SGIC_Leave.model.LeaveBalance;
import com.leave.SGIC_Leave.model.LeaveType;
import com.leave.SGIC_Leave.repository.EmployeeRepository;
import com.leave.SGIC_Leave.repository.LeaveBalanceRepository;
import com.leave.SGIC_Leave.repository.LeaveTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class LeaveTypeService {

    private final LeaveTypeRepository leaveTypeRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;

    @Autowired
    public LeaveTypeService(LeaveTypeRepository leaveTypeRepository,
                            EmployeeRepository employeeRepository,
                            LeaveBalanceRepository leaveBalanceRepository) {
        this.leaveTypeRepository = leaveTypeRepository;
        this.employeeRepository = employeeRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
    }

    public LeaveType addLeaveType(LeaveType leaveType){
        LeaveType savedLeaveType = leaveTypeRepository.save(leaveType);

        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees){
            LeaveBalance leaveBalance = new LeaveBalance();
            leaveBalance.setEmployee(employee);
            leaveBalance.setLeaveType(savedLeaveType);

            if (employee.getHireDate() != null &&
                    Period.between(employee.getHireDate(), LocalDate.now()).getYears() >= 1) {
                leaveBalance.setBalance(savedLeaveType.getYearLeave());
            } else {
                leaveBalance.setBalance(0);
            }

            leaveBalanceRepository.save(leaveBalance);
        }

        return savedLeaveType;
    }
}
