package com.example.reactivevsslow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@SpringBootApplication
public class SlowBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SlowBackendApplication.class, args);
    }

}

@RestController
class SlowPersonController {
    @GetMapping("/person")
    @SneakyThrows
    Person getSlowPerson() {
        Thread.sleep(5000);

        return new Person(UUID.randomUUID().toString(), "John");
    }
}

@RestController
class SlowItemController {
    @GetMapping("/item")
    @SneakyThrows
    Item getSlowItem() {
        Thread.sleep(5000);
        return new Item(UUID.randomUUID().toString(), "Shovel");
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Person {
    private String uuid;
    private String name;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Item {
    private String uuid;
    private String name;
}

