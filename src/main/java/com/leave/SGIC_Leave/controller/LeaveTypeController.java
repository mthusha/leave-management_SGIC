package com.leave.SGIC_Leave.controller;


import com.leave.SGIC_Leave.CONST.Const;
import com.leave.SGIC_Leave.model.LeaveType;
import com.leave.SGIC_Leave.service.LeaveTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Const.ADD_LEAVE_TYPE)
public class LeaveTypeController {


    private final LeaveTypeService leaveTypeService;

    @Autowired
    public LeaveTypeController (LeaveTypeService leaveTypeService){
        this.leaveTypeService = leaveTypeService;
    }

    @PostMapping
    public ResponseEntity<LeaveType> addLeaveType (@RequestBody LeaveType leaveType){
        LeaveType newLeveatype = leaveTypeService.addLeaveType(leaveType);
        return new ResponseEntity<>(newLeveatype, HttpStatus.CREATED);
    }
}
