package net.javaTestProj.springboot.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaTestProj.springboot.model.Employee;
import net.javaTestProj.springboot.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;

@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc; //lets you test your web controllers by pretending to send web browser requests and checking the responses,without running a web server.

    @MockBean //tells Spring to create a mock object of EmployeeService and register it in application context so it can be used by EmployeeController
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper; //used to convert Java objects to JSON (serialization) and JSON to Java objects (deserialization)

    //JUnit test for Create Employee REST API
//    @Test
//    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
//        //given - precondition or setup
//        Employee employee = Employee.builder().
//                firstName("Ambar").
//                lastName("Adhikari").
//                email("ambar@cts.com").
//                build();
//
//        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
//                .willAnswer((invocation) -> invocation.getArgument(0));
//
//        //when - action or the behaviour that we are going test
//        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(employee)));
//
//        //then - verify the output
//        response.andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
//
//    }

    // For this test case the response from controller is structured differently than what was initially assumed.
//    {
//        "Message": "New employee added",
//            "HttpStatus": "CREATED",
//            "Data": {
//                "id": 1,
//                "firstName": "Ambar",
//                "lastName": "Adhikari",
//                "email": "ambar@cts.com"
//              }
//    }
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given - precondition or setup
        Employee employee = Employee.builder().
                firstName("Ambar").
                lastName("Adhikari").
                email("ambar@cts.com").
                build();

        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

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

        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/"));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(listOfEmployees.size())));

    }

    //JUnit test for GET employee by ID REST API - Positive scenario - valid employee id
    // For this test case the response from controller is structured differently than what was initially assumed.
//    {
//        "Message": "Employee detail is given",
//            "HttpStatus": "OK",
//            "Data": {
//                "id": 1,
//                "firstName": "Ambar",
//                "lastName": "Adhikari",
//                "email": "ambar@cts.com"
//              }
//    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //given - precondition or setup
        long employeeId = -2L;
        Employee employee = Employee.builder().
                firstName("Ambar").
                lastName("Adhikari").
                email("ambar@cts.com").
                build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}",employeeId));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Message", is("Employee detail is given")))
                .andExpect(jsonPath("$.HttpStatus", is("OK")))
                .andExpect(jsonPath("$.Data.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.Data.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.Data.email", is(employee.getEmail())));

        //** in employeeId = -2L , we can pass any id and it will pass test case
        //You can't get the EXACT Id value in the response because we haven't set the id in the Employee build object.
        //If you want id in the response then set Id(1L) to the above object.
        //In a real-time project, we get the id in the response because it exists in the database.
    }

    //JUnit test for GET employee by ID REST API - Negative scenario - Invalid employee id
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        //given - precondition or setup
        long employeeId = -2L;
//        Employee employee = Employee.builder().
//                firstName("Ambar").
//                lastName("Adhikari").
//                email("ambar@cts.com").
//                build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}",employeeId));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    //JUnit test for Update Employee by REST API - Positive scenario
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {
        //given - precondition or setup
        long employeeId = 1L;

        Employee savedEmployee = Employee.builder(). //assume savedEmployee we are getting from database
                firstName("Ambar").
                lastName("Adhikari").
                email("ambar@cts.com").
                build();

        Employee updatedEmployee = Employee.builder().
                firstName("Anshu").
                lastName("MS Adhikari").
                email("Anshu@gmail.com").
                build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}",employeeId)
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
        long employeeId = 1L;

        Employee savedEmployee = Employee.builder(). //assume savedEmployee we are getting from database
                firstName("Ambar").
                lastName("Adhikari").
                email("ambar@cts.com").
                build();

        Employee updatedEmployee = Employee.builder().
                firstName("Anshu").
                lastName("MS Adhikari").
                email("Anshu@gmail.com").
                build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty()); //getEmployeeById is returning empty object
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}",employeeId)
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
        long employeeId = 1L;

        willDoNothing().given(employeeService).deleteEmployee(employeeId);

        //when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}",employeeId));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isOk());

    }
}
