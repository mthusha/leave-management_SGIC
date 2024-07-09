package com.leave.SGIC_Leave.controller;


import com.leave.SGIC_Leave.CONST.Const;
import com.leave.SGIC_Leave.CONST.Status;
import com.leave.SGIC_Leave.model.Employee;
import com.leave.SGIC_Leave.model.LeaveRequest;
import com.leave.SGIC_Leave.service.EmployeeService;
import com.leave.SGIC_Leave.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Const.SEND_LEAVE_REQUEST)
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;
    private final EmployeeService employeeService;


    @Autowired
    public LeaveRequestController(LeaveRequestService leaveRequestService, EmployeeService employeeService) {
        this.leaveRequestService = leaveRequestService;
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<LeaveRequest> createLeaveRequest(@RequestBody LeaveRequest leaveRequest) {
        LeaveRequest newLeaveRequest = leaveRequestService.createLeaveRequest(leaveRequest);
        return new ResponseEntity<>(newLeaveRequest, HttpStatus.CREATED);
    }
    @GetMapping(Const.ALL_PENDING_LIST)
    public ResponseEntity<List<LeaveRequest>> getAllPendingLeaveRequests() {
        List<LeaveRequest> leaveRequests = leaveRequestService.getAllPendingLeaveRequests();
        return new ResponseEntity<>(leaveRequests, HttpStatus.OK);
    }

    @PatchMapping(Const.UPDATE_STATUS)
    public ResponseEntity<LeaveRequest> updateLeaveRequestStatus(@PathVariable Long id, @RequestParam String status) {
        Status newStatus;
        try {
            newStatus = Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LeaveRequest updatedLeaveRequest = leaveRequestService.updateLeaveRequestStatus(id, newStatus);
        return new ResponseEntity<>(updatedLeaveRequest, HttpStatus.OK);
    }
    @GetMapping(Const.ALL_LEAVE_DATA_EMPLOYEE_ID)
    public ResponseEntity<List<LeaveRequest>> getAllLeaveRequestsByEmployeeId(@PathVariable Long employeeId) {
        List<LeaveRequest> leaveRequests = leaveRequestService.getAllLeaveRequestsByEmployeeId(employeeId);
        return new ResponseEntity<>(leaveRequests, HttpStatus.OK);
    }
}
