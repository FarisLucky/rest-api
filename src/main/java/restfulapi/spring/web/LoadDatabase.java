package restfulapi.spring.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import restfulapi.spring.web.entity.Employee;
import restfulapi.spring.web.entity.Order;
import restfulapi.spring.web.entity.enums.Status;
import restfulapi.spring.web.repository.EmployeeRepository;
import restfulapi.spring.web.repository.OrderRepository;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    public CommandLineRunner initDatabase(EmployeeRepository employeeRepository, OrderRepository orderRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                employeeRepository.save(new Employee("Salman","Faris", "Admin"));
                employeeRepository.save(new Employee("Muhammad","Farid", "Admin"));
                employeeRepository.findAll().forEach(employee -> log.info("PreLoad "+employee));
                orderRepository.save(new Order("Lenovo ThinkPad", Status.COMPLETED));
                orderRepository.save(new Order("Mobilio", Status.IN_PROGRESS));
                orderRepository.findAll().forEach(order -> log.info("PreLoad "+order));
            }
        };
    }
}
