package com.example.reactiveconsumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
public class ReactiveConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveConsumerApplication.class, args);
    }

    @Bean
    WebClient client() {
        return WebClient.create("http://localhost:8080");
    }
}

@RestController
class ReactiveController {

    @Autowired
    WebClient webClient;

    @GetMapping
    Mono<PersonItem> getPersonItem() {
        long start = System.currentTimeMillis();
        var personMono = webClient.get().uri("/person").retrieve().bodyToMono(Person.class)
                .subscribeOn(Schedulers.elastic());
        var itemMono = webClient.get().uri("/item").retrieve().bodyToMono(Item.class)
                .subscribeOn(Schedulers.elastic());

        Mono<PersonItem> personItemMono = Mono.zip(personMono, itemMono, (person, item) ->
                new PersonItem(person.getUuid(), person.getName(), item.getUuid(), item.getName()));

        long finish = System.currentTimeMillis();
        System.out.println("Time taken: " + (finish - start)); // ~0 seconds
        return personItemMono;
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class PersonItem {
    private String personUuid;
    private String personName;
    private String itemUuid;
    private String itemName;
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
