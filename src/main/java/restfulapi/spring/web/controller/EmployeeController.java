package restfulapi.spring.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import restfulapi.spring.web.domain.Employee;
import restfulapi.spring.web.exception.EmployeeNotFoundException;
import restfulapi.spring.web.repository.EmployeeRepository;
import java.util.List;
import java.util.stream.Collectors;
import restfulapi.spring.web.payload.EmployeeModelResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final EmployeeModelResponse employeeModelResponse;
    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository, EmployeeModelResponse employeeModelResponse) {
        this.employeeRepository = employeeRepository;
        this.employeeModelResponse = employeeModelResponse;
    }

    @GetMapping("/employees")
    public CollectionModel<EntityModel<Employee>> all() {
        List<EntityModel<Employee>> employees = employeeRepository.findAll().stream()
                .map(employeeModelResponse::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(employees,
                linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }

    @PostMapping("/employees")
    public ResponseEntity<?> newEmployee(@RequestBody Employee employee) {
        EntityModel<Employee> entityModel = employeeModelResponse.toModel(employeeRepository.save(employee));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/employees/{id}")
    public EntityModel<Employee> one(@PathVariable("id") Long id) {
        Employee employeeData = employeeRepository.findById(id)
                .orElseThrow(()->new EmployeeNotFoundException(id));
        return employeeModelResponse.toModel(employeeData);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        Employee updateEmployee = employeeRepository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return employeeRepository.save(employee);
                })
                .orElseGet(()->{
                    newEmployee.setId(id);
                    return employeeRepository.save(newEmployee);
                });
        EntityModel<Employee> entityModel = employeeModelResponse.toModel(updateEmployee);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") Long id) {
        employeeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
