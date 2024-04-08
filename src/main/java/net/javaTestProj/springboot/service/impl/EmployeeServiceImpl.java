package net.javaTestProj.springboot.service.impl;

import net.javaTestProj.springboot.exception.DuplicateResourceException;
import net.javaTestProj.springboot.exception.ResourceNotFoundException;
import net.javaTestProj.springboot.model.Employee;
import net.javaTestProj.springboot.repository.EmployeeRepository;
import net.javaTestProj.springboot.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {

        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());

        if(savedEmployee.isPresent()){
            throw new DuplicateResourceException("Employee already exist with given email : "+employee.getEmail());
        }

        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isEmpty()){
            throw new ResourceNotFoundException("Employee not found with given id : "+id);
        }

        return employee;
    }

    @Override
    public Employee updateEmployee(Employee updatedEmployee) {
        return employeeRepository.save(updatedEmployee);
    }

    @Override
    public void deleteEmployee(long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isEmpty()){
            throw new ResourceNotFoundException("Employee not found with given id : "+id);
        }
        employeeRepository.deleteById(id);
    }
}
