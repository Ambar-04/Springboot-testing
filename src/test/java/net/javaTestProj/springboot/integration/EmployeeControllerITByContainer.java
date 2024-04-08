package net.javaTestProj.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaTestProj.springboot.model.Employee;
import net.javaTestProj.springboot.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITByContainer extends AbstractContainerBaseTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        employeeRepository.deleteAll();
    }

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        //given - precondition or setup
        Employee employee = Employee.builder().
                firstName("Ambar").
                lastName("Adhikari").
                email("ambar@cts.com").
                build();
//          NO NEED TO MOCK IN INTEGRATION TESTING
//        given(employeeService.saveEmployee(any(Employee.class)))
//                .willAnswer((invocation) -> invocation.getArgument(0));

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/employees/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.Message", is("New employee added")))
                .andExpect(jsonPath("$.HttpStatus", is("CREATED")))
                .andExpect(jsonPath("$.Data.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.Data.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.Data.email", is(employee.getEmail())));

    }


    //JUnit test for Get all employees REST API
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeeList() throws Exception {
        //given - precondition or setup
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("Ambar").lastName("Adhikari").email("ambar@cts.com").build());
        listOfEmployees.add(Employee.builder().firstName("MS").lastName("Ad").email("MS@gmail.com").build());

        employeeRepository.saveAll(listOfEmployees); //Saving into database

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/"));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));

    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //given - precondition or setup
        Employee employee = Employee.builder().
                firstName("Ambar").
                lastName("Adhikari").
                email("ambar@cts.com").
                build();

        employeeRepository.save(employee);

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId())); //Need to pass id from database

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Message", is("Employee detail is given")))
                .andExpect(jsonPath("$.HttpStatus", is("OK")))
                .andExpect(jsonPath("$.Data.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.Data.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.Data.email", is(employee.getEmail())));
    }

    //JUnit test for GET employee by ID REST API - Negative scenario - Invalid employee id
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        //given - precondition or setup
        long employeeId = -2L;
        Employee employee = Employee.builder().
                firstName("Ambar").
                lastName("Adhikari").
                email("ambar@cts.com").
                build();

        employeeRepository.save(employee);

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId)); //passing invalid employee id

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }


    //JUnit test for Update Employee by REST API - Positive scenario
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {
        //given - precondition or setup
        Employee savedEmployee = Employee.builder(). //assume savedEmployee we are getting from database
                firstName("Ambar").
                lastName("Adhikari").
                email("ambar@cts.com").
                build();
        employeeRepository.save(savedEmployee); //saving into database

        Employee updatedEmployee = Employee.builder().
                firstName("Anshu").
                lastName("MS Adhikari").
                email("Anshu@gmail.com").
                build();

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}",savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    //JUnit test for Update Employee by REST API - Negative scenario
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {
        //given - precondition or setup
        long employeeId = -1L;

        Employee savedEmployee = Employee.builder().
                firstName("Ambar").
                lastName("Adhikari").
                email("ambar@cts.com").
                build();

        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder().
                firstName("Anshu").
                lastName("MS Adhikari").
                email("Anshu@gmail.com").
                build();


        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}",employeeId) //passing invalid employee id
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    //JUnit test for Delete employee REST API
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        //given - precondition or setup
        Employee savedEmployee = Employee.builder().
                firstName("Ambar").
                lastName("Adhikari").
                email("ambar@cts.com").
                build();

        employeeRepository.save(savedEmployee);

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}",savedEmployee.getId()));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk());

    }

}
