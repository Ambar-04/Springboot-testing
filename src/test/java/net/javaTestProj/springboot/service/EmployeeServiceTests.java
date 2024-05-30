package net.javaTestProj.springboot.service;

import net.javaTestProj.springboot.exception.ResourceNotFoundException;
import net.javaTestProj.springboot.model.Employee;
import net.javaTestProj.springboot.repository.EmployeeRepository;
import net.javaTestProj.springboot.service.impl.EmployeeServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    //Mocking: instead of calling real method you can specify what to return or do when the method is called.
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setUp(){
          //employeeRepository = Mockito.mock(EmployeeRepository.class);
          //employeeService = new EmployeeServiceImpl(employeeRepository);

        employee = Employee.builder().
                firstName("Ambar").
                lastName("Adhikari").
                email("ambar@cts.com").
                build();
    }

    //JUnit test for saveEmployee method
    @Test
    @DisplayName("JUnit test for saveEmployee method")
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject(){
        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        System.out.println(employeeRepository);
        System.out.println(employeeService);

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeService.saveEmployee(employee);

        System.out.println(savedEmployee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();

    }

    //JUnit test for saveEmployee method which throws Exception
    @Test
    @DisplayName("JUnit test for saveEmployee method which throws Exception")
    public void givenExistingEmail_whenSaveEmployee_throwsException(){
        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee)); // as we want exception ,so we want to have employee of same email

        //given(employeeRepository.save(employee)).willReturn(employee); //Extra stubbing not required, as after exception control won't go to this method in serviceImpl class

        System.out.println(employeeRepository);
        System.out.println(employeeService);

        //when - action or the behaviour that we are going test
        /* asserThrows:
         **check document**
         assert that execution of the supplied executable(lambda) throws an exception of the expectedType and return the exception.
         Here the saveEmployee() is the executable method which throws exception from service class.
         If no exception is thrown, or if an exception of a different type is thrown, this method will fail.
        */

        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        //then
         /*
         **check document**
        writing this so that the code doesn't execute after the exception of if block in serviceImpl class
        This line uses Mockito's verify method to check that the save method of employeeRepository is never called
        with any Employee object passed as an argument. If it's called, the test will fail.
         */
        verify(employeeRepository, never()).save(any(Employee.class));  // any() indicates that the method should match any argument of type Employee.
    }

    //JUnit test for getAllEmployees method
    @Test
    @DisplayName("JUnit test for getAllEmployees method")
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList(){
        //given - precondition or setup
        Employee employee1 = Employee.builder().
                firstName("Leo").
                lastName("Messi").
                email("LM10@barca.com").
                build();
        
        given(employeeRepository.findAll()).willReturn(List.of(employee,employee1));

        //when - action or the behaviour that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees(); //this will internally call findAll(), which will return List.of(), and we stubbed findAll()

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    //JUnit test for getAllEmployees method - negative scenario
    @Test
    @DisplayName("JUnit test for getAllEmployees method - negative scenario")
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeeList(){
        //given - precondition or setup

        given(employeeRepository.findAll()).willReturn(Collections.emptyList()); //Empty list

        //when - action or the behaviour that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees(); //this will internally call findAll(), which will return empty list, and we stubbed findAll()

        //then - verify the output
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    //JUnit test for getEmployeeById method
    @Test
    @DisplayName("JUnit test for getEmployeeById method")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){
        //given - precondition or setup
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));

        //when - action or the behaviour that we are going test
        Optional<Employee> savedEmployee = employeeService.getEmployeeById(employee.getId());

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //JUnit test for updateEmployee method
    @Test
    @DisplayName("JUnit test for updateEmployee method")
    public void givenEmployeeObject_whenUpdatedEmployee_thenReturnUpdatedEmployee(){
        //given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);

        employee.setFirstName("Anshu");
        employee.setEmail("Anshu@cts.com");

        //when - action or the behaviour that we are going test
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        //then - verify the output
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Anshu");
        assertThat(updatedEmployee.getEmail()).isEqualTo("Anshu@cts.com");
        assertThat(updatedEmployee).isNotNull();
    }


    //JUnit test for deleteEmployee method
    // where an existing employee is successfully deleted. It ensures that the findById method is called once
    // with the specified ID and that the deleteById method is also called once.
    @Test
    @DisplayName("JUnit test for deleteEmployee method")
    public void givenEmployeeId_whenDeleteEmployee_thenNothing(){
        long empId = 1L;

        //given
        // Stubbing findById to return an existing employee
        given(employeeRepository.findById(empId)).willReturn(Optional.of(employee));

        //when
        // Call deleteEmployee method
        employeeService.deleteEmployee(empId);

        //then
        // Verify that findById was called once with the specified ID
        verify(employeeRepository, times(1)).findById(empId);

        // Verify that deleteById was called once with the specified ID
        verify(employeeRepository, times(1)).deleteById(empId);
    }

    //JUnit test for deleteEmployee method
    //where a non-existing employee ID is provided for deletion. It ensures that the findById method is called once
    //with the specified ID and that the deleteById method is not called.
    //It also verifies that a ResourceNotFoundException is thrown with the correct error message.
    @Test
    @DisplayName("Delete non-existing employee")
    void givenNonExistingEmployeeId_whenDeleteEmployee_thenResourceNotFoundExceptionThrown() {
        long empId = 1L;

        //given
        // Stubbing findById to return an empty Optional
        given(employeeRepository.findById(empId)).willReturn(Optional.empty());

        //when
        // Call deleteEmployee method and assert ResourceNotFoundException is thrown
        //assertThrows(ResourceNotFoundException.class, () -> employeeService.deleteEmployee(empId));
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> employeeService.deleteEmployee(empId));

        System.out.println(exception.getMessage());
        //then
        // Verify that findById was called once with the specified ID
        verify(employeeRepository, times(1)).findById(empId);

        // Verify that deleteById was not called
        verify(employeeRepository, never()).deleteById(empId);

        // Assert the exception message
        /* this we can call when we are creating that object of class 'ResourceNotFoundException', or else we can't.
         Syntax: assertEquals(expected, actual)
         We use assertEquals to verify that the result of the actual operation is equal to the expected
         If the actual value/method returns matches the expected value/method returns, the test will pass.
         Otherwise, it will fail, indicating that something unexpected has occurred
         */
        assertEquals("Employee not found with given id : " + empId, exception.getMessage());
    }
}
