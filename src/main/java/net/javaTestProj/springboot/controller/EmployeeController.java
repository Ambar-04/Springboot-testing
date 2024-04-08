package net.javaTestProj.springboot.controller;

import net.javaTestProj.springboot.exception.DuplicateResourceException;
import net.javaTestProj.springboot.exception.ResourceNotFoundException;
import net.javaTestProj.springboot.model.Employee;
import net.javaTestProj.springboot.response.ResponseHandler;
import net.javaTestProj.springboot.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees/")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Object> createEmployee(@RequestBody Employee employee) {
        //return new ResponseEntity<>(employeeService.saveEmployee(employee), HttpStatus.CREATED);
        try {
            Employee savedEmployee = employeeService.saveEmployee(employee);
            return ResponseHandler.responseHandler(HttpStatus.CREATED, "New employee added", savedEmployee);
        } catch (DuplicateResourceException e) {
            return ResponseHandler.responseHandler(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }


    @GetMapping
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

//    @GetMapping("{id}")
//    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long employeeId){
//        return employeeService.getEmployeeById(employeeId).map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }

//    @GetMapping("{id}")
//    public ResponseEntity<Object> getEmployeeById(@PathVariable("id") long employeeId) {
//        Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
//
//        if (employee.isPresent()) {
//            return ResponseHandler.responseHandler(HttpStatus.OK, "Employee detail is given", employee.get());
//        } else{
//            return ResponseHandler.responseHandler(HttpStatus.NOT_FOUND, "Employee not found", null);
//        }
//    }
    @GetMapping("{id}")
    public ResponseEntity<Object> getEmployeeById(@PathVariable("id") long employeeId) {
        try{
            Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
            return ResponseHandler.responseHandler(HttpStatus.OK, "Employee detail is given", employee.get());
        }catch (ResourceNotFoundException e){
            return ResponseHandler.responseHandler(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

//    @PutMapping("{id}")
//    public ResponseEntity<Object> updateEmployee(@PathVariable("id") long employeeId, @RequestBody Employee employee){
//        return employeeService.getEmployeeById(employeeId)
//                .map(savedEmployee -> {
//
//                    savedEmployee.setFirstName(employee.getFirstName());
//                    savedEmployee.setLastName(employee.getLastName());
//                    savedEmployee.setEmail(employee.getEmail());
//
//                    Employee updatedEmployee = employeeService.updateEmployee(savedEmployee);
//                    return ResponseHandler.responseHandler(HttpStatus.OK, "Employee updated successfully", updatedEmployee);
//                })
//                .orElseGet(() -> ResponseHandler.responseHandler(HttpStatus.NOT_FOUND, "Employee not found for update", null));
//    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateEmployee(@PathVariable("id") long employeeId, @RequestBody Employee employee) {
        ResponseEntity<Object> response;

        Optional<Employee> optionalEmployee = employeeService.getEmployeeById(employeeId);

        if (optionalEmployee.isPresent()) {
            Employee savedEmployee = optionalEmployee.get();
            savedEmployee.setFirstName(employee.getFirstName());
            savedEmployee.setLastName(employee.getLastName());
            savedEmployee.setEmail(employee.getEmail());

            Employee updatedEmployee = employeeService.updateEmployee(savedEmployee);
            response = ResponseHandler.responseHandler(HttpStatus.OK, "Employee updated successfully", updatedEmployee);
        } else {
            response = ResponseHandler.responseHandler(HttpStatus.NOT_FOUND, "Employee not found for update", null);
        }

        return response;
    }


//    @DeleteMapping("{id}")
//    public ResponseEntity<Object> deleteEmployee(@PathVariable("id") long employeeId) {
//        try {
//            Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
//            if (employee.isPresent()) {
//                employeeService.deleteEmployee(employeeId);
//                return ResponseHandler.responseHandler(HttpStatus.OK, "Successfully deleted the employee !!!!", null);
//            } else {
//                // Employee not found
//                return ResponseHandler.responseHandler(HttpStatus.NOT_FOUND, "Employee not found", null);
//            }
//        } catch (ResourceNotFoundException e) {
//            return ResponseHandler.responseHandler(HttpStatus.NOT_FOUND, e.getMessage(), null);
//        }
//    }

    // Try to understand how to make code easier, here try ctach is making code more complex,so better to use if else
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable("id") long employeeId) {
        Optional<Employee> employee = employeeService.getEmployeeById(employeeId);
        if(employee.isPresent()){
            return ResponseHandler.responseHandler(HttpStatus.OK, "Successfully deleted the employee !!!!", null);
        }
        return ResponseHandler.responseHandler(HttpStatus.NOT_FOUND, "Employee not found", null);
    }



}
