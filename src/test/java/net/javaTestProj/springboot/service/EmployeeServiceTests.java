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
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));
        //given(employeeRepository.save(employee)).willReturn(employee); //Extra stubbing not required

        System.out.println(employeeRepository);
        System.out.println(employeeService);

        //when - action or the behaviour that we are going test
        /* asserThrows:
         "Assert" that execution of the supplied executable throws an exception of the expectedType and return the exception.
         Here the saveEmployee() is the executable method which throws exception from service class.
         If no exception is thrown, or if an exception of a different type is thrown, this method will fail.If you do not want to perform additional checks on the exception instance,ignore the return value.*/

        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        //then
        //writing this so that the code doesn't execute after the exception of if block in serviceImpl class
        verify(employeeRepository, never()).save(any(Employee.class));
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
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    //JUnit test for getAllEmployees method - negative scenario
    @Test
    @DisplayName("JUnit test for getAllEmployees method - negative scenario")
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeeList(){
        //given - precondition or setup
        Employee employee1 = Employee.builder().
                firstName("Leo").
                lastName("Messi").
                email("LM10@barca.com").
                build();

        given(employeeRepository.findAll()).willReturn(Collections.emptyList()); //Empty list

        //when - action or the behaviour that we are going test
        List<Employee> employeeList = employeeService.getAllEmployees();

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
    @Test
    @DisplayName("JUnit test for deleteEmployee method")
    public void givenEmployeeId_whenDeleteEmployee_thenNothing(){
        //given - precondition or setup

        willDoNothing().given(employeeRepository).deleteById(employee.getId());

        //when - action or the behaviour that we are going test
        employeeService.deleteEmployee(employee.getId());

        //then - verify the output
        //As delete method is not returning anything so we can't validate/verify output
        //So we will validate how many times deleteById has been called
        verify(employeeRepository, times(1)).deleteById(employee.getId());
    }
}
