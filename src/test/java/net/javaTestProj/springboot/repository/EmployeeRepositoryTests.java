package net.javaTestProj.springboot.repository;

import net.javaTestProj.springboot.model.Employee;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setUp(){
        employee = Employee.builder()
                .firstName("Ambar")
                .lastName("Adhikari")
                .email("ambar@cts.com")
                .build();
    }

    //JUnit test for save employee operation
    @Test
    @DisplayName("JUnit test for save employee operation")
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){

        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ambar")
//                .lastName("Adhikari")
//                .email("ambar@cts.com")
//                .build();
        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }


    //JUnit test for get all employees operation
    @Test
    @DisplayName("JUnit test for get all employees operation")
    public void givenEmployeesList_whenFindAll_thenEmployeesListFound(){

        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ambar")
//                .lastName("Adhikari")
//                .email("ambar@cts.com")
//                .build();

        Employee employee2 = Employee.builder()
                .firstName("MS")
                .lastName("Adhikari")
                .email("MS@rt.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        //when - action or the behaviour that we are going test
        List<Employee> employeeList = employeeRepository.findAll();

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isGreaterThan(0);
        assertThat(employeeList.size()).isEqualTo(2);
    }

    //JUnit test for get all employees operation - Negative scenario
    @Test
    @DisplayName("JUnit test for get all employees operation - Negative scenario")
    public void givenEmployeesList_whenFindAll_thenEmployeesListNotFound(){

        //given - precondition or setup

        //when - action or the behaviour that we are going test
        List<Employee> employeeList = employeeRepository.findAll();

        //then - verify the output
//        assertThat(employeeList).isNull(); //'null' represents the absence of a value or the lack of an object reference.you can assign null to indicate that it currently has no reference to any object.
        assertThat(employeeList).isNotNull(); //Verify that the list is not 'null'.
        assertThat(employeeList).isEmpty(); //Verify that the list is empty. [] represents an 'empty' array, meaning an array that contains no elements.
        assertThat(employeeList.size()).isEqualTo(0);
    }

    //JUnit test for get employee by ID operation
        @Test
        @DisplayName("JUnit test for get employee by ID operation")
        public void givenEmployee_whenFindById_thenReturnEmployeeObject(){
            //given - precondition or setup
//            Employee employee = Employee.builder()
//                    .firstName("Ambar")
//                    .lastName("Adhikari")
//                    .email("ambar@cts.com")
//                    .build();

            employeeRepository.save(employee);

            //when - action or the behaviour that we are going test
            Employee saved_Employee = employeeRepository.findById(employee.getId()).get();

            //then - verify the output
            assertThat(saved_Employee).isNotNull();
        }


    @Test
    @DisplayName("JUnit test for get employee by ID operation - Negative Scenario")
    public void givenInvalidEmployeeId_whenFindById_thenEmployeeNotFound() {
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ambar")
//                .lastName("Adhikari")
//                .email("ambar@cts.com")
//                .build();

        employeeRepository.save(employee);

        //when - action or the behavior that we are going to test
        // Attempt to find an employee with an invalid ID (non-existent(-1) ID)
        Optional<Employee> retrievedEmployee = employeeRepository.findById(-1L);

        //then - verify the output
        assertThat(retrievedEmployee).isEmpty(); // Expecting that no employee is found with the given invalid ID
    }

    //JUnit test for get employee by email operation
    @Test
    @DisplayName("JUnit test for get employee by email operation")
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject(){
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ambar")
//                .lastName("Adhikari")
//                .email("ambar@cts.com")
//                .build();

        employeeRepository.save(employee);

        //when - action or the behaviour that we are going test
        Optional<Employee> saved_Employee = employeeRepository.findByEmail(employee.getEmail());

        //then - verify the output
        assertThat(saved_Employee).isNotNull();
    }

    //JUnit test for get employee by email operation - Negative Scenario
    @Test
    @DisplayName("JUnit test for get employee by email operation - Negative Scenario")
    public void givenEmployeeEmail_whenFindByEmail_thenNotReturnEmployeeObject(){
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ambar")
//                .lastName("Adhikari")
//                .email("ambar@cts.com")
//                .build();

        employeeRepository.save(employee);

        //when - action or the behaviour that we are going test
        Optional<Employee> saved_Employee = employeeRepository.findByEmail("abc@gmail.com");

        //then - verify the output
        assertThat(saved_Employee).isEmpty();
        //assertThat(saved_Employee).isNull(); In Java, Optional.empty() is different from null. If a method returns an Optional, it's best to use Optional methods for assertions rather than checking for null
    }

    //JUnit test for update employee operation
    @Test
    @DisplayName("JUnit test for update employee operation")
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ambar")
//                .lastName("Adhikari")
//                .email("ambar@cts.com")
//                .build();

        employeeRepository.save(employee);

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();

        savedEmployee.setFirstName("MS");
        savedEmployee.setEmail("MS.adhikari@cts.com");

        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        //then - verify the output
        assertThat(updatedEmployee.getFirstName()).isEqualTo("MS");
        assertThat(updatedEmployee.getEmail()).isEqualTo("MS.adhikari@cts.com");
    }

    //JUnit test for delete employee operation
    @Test
    @DisplayName("JUnit test for delete employee operation")
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee(){
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ambar")
//                .lastName("Adhikari")
//                .email("ambar@cts.com")
//                .build();

        employeeRepository.save(employee);

        //when - action or the behaviour that we are going test
        //employeeRepository.delete(employee);
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        //then - verify the output
        assertThat(employeeOptional).isEmpty();
    }


    //JUnit test for custom query using JPQL with index parameters
    @Test
    @DisplayName("JUnit test for custom query using JPQL with index parameters")
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject(){
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ambar")
//                .lastName("Adhikari")
//                .email("ambar@cts.com")
//                .build();

        employeeRepository.save(employee);

        String firstName = "Ambar";
        String lastName = "Adhikari";

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByJPQL(firstName,lastName);

        //then - verify the output
        assertThat(savedEmployee.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(savedEmployee.getLastName()).isEqualTo(employee.getLastName());
        assertThat(savedEmployee).isNotNull();
    }

    //JUnit test for custom query using JPQL with index parameters - Negative Scenario
    @Test
    @DisplayName("JUnit test for custom query using JPQL with index parameters - Negative Scenario")
    public void givenFirstNameAndLastName_whenFindByJPQL_thenNotReturnEmployeeObject(){
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ambar")
//                .lastName("Adhikari")
//                .email("ambar@cts.com")
//                .build();

        employeeRepository.save(employee);

        String firstName = "MS";
        String lastName = "Jana";

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByJPQL(firstName,lastName);

        //then - verify the output
        // because "savedEmployee" is null, so we won't get any firstName and lastName in "savedEmployee" to check
        // assertThat(savedEmployee.getFirstName()).isNotEqualTo(employee.getFirstName());
        // assertThat(savedEmployee.getLastName()).isNotEqualTo(employee.getLastName());
        assertThat(savedEmployee).isNull();
    }


    //JUnit test for custom query using JPQL with named parameters
    @Test
    @DisplayName("JUnit test for custom query using JPQL with named parameters")
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject(){
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ambar")
//                .lastName("Adhikari")
//                .email("ambar@cts.com")
//                .build();

        employeeRepository.save(employee);

        String firstName = "Ambar";
        String lastName = "Adhikari";

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(firstName,lastName);

        //then - verify the output
        assertThat(savedEmployee.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(savedEmployee.getLastName()).isEqualTo(employee.getLastName());
        assertThat(savedEmployee).isNotNull();
    }

    //JUnit test for custom query using JPQL with named parameters - Negative Scenario
    @Test
    @DisplayName("JUnit test for custom query using JPQL with named parameters - Negative Scenario")
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenNotReturnEmployeeObject(){
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ambar")
//                .lastName("Adhikari")
//                .email("ambar@cts.com")
//                .build();

        employeeRepository.save(employee);

        String firstName = "MS";
        String lastName = "Adhikari"; //last name is still the same

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(firstName,lastName);
        System.out.println("Saved : " + savedEmployee); //even if only 1 is matching it will return NULL as in AND condition both should match

        //then - verify the output
        assertThat(savedEmployee).isNull();
    }


    //JUnit test for custom query using native SQL with index
    @Test
    @DisplayName("JUnit test for custom query using native SQL with index")
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject(){
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ambar")
//                .lastName("Adhikari")
//                .email("ambar@cts.com")
//                .build();

        employeeRepository.save(employee);

        //String firstName = "Ambar";
        //String lastName = "Adhikari";

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByNativeSQL(employee.getFirstName(), employee.getLastName());

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //JUnit test for custom query using native SQL with named params
    @Test
    @DisplayName("JUnit test for custom query using native SQL with named params")
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject(){
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ambar")
//                .lastName("Adhikari")
//                .email("ambar@cts.com")
//                .build();

        employeeRepository.save(employee);

        //String firstName = "Ambar";
        //String lastName = "Adhikari";

        //when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByNativeSQLNamedParams(employee.getFirstName(), employee.getLastName());

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }


}
