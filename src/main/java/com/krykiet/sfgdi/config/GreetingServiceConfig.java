package com.krykiet.sfgdi.config;

import com.krykiet.sfgdi.datasource.FakeDataSource;
import com.krykiet.sfgdi.repositories.EnglishGreetingRepository;
import com.krykiet.sfgdi.repositories.EnglishGreetingRepositoryImpl;
import com.krykiet.sfgdi.services.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import other.krykiet.pets.DogPetService;
import other.krykiet.pets.PetService;
import other.krykiet.pets.PetServiceFactory;

// We can get rid of this using spring boot, we can just put data in application.properties
//@PropertySource("classpath:datasource.properties")
@ImportResource("classpath:sfgdi-config.xml") // we need to tell spring to import the resource for setting the context
@Configuration
public class GreetingServiceConfig {

    // Property directions in ${property.value} CURLY BRACKETS
    @Bean
    FakeDataSource fakeDataSource(@Value("${krykiet.username}") String username,
                                  @Value("${krykiet.password}") String password,
                                  @Value("${krykiet.jdbcurl}") String jdbcurl) {
        FakeDataSource fakeDataSource = new FakeDataSource();
        fakeDataSource.setUsername(username);
        fakeDataSource.setPassword(password);
        fakeDataSource.setJdbcurl(jdbcurl);
        return fakeDataSource;
    }

    @Bean
    PetServiceFactory petServiceFactory(){
        return new PetServiceFactory();
    }

    @Profile({"dog", "default"})
    @Bean
    PetService dogPetService(PetServiceFactory petServiceFactory){
        return petServiceFactory.getPetService("dog");
    }

    @Profile("cat")
    @Bean
    PetService catPetService(PetServiceFactory petServiceFactory) {
        return petServiceFactory.getPetService("cat");
    }

    @Profile({"ES", "default"})
    @Bean("i18nService") // by default bean name is method name, unless we override it here
    I18nSpanishGreetingService i18nSpanishGreetingService() {
        return new I18nSpanishGreetingService();
    }

    @Bean
    EnglishGreetingRepository englishGreetingRepository() {
        return new EnglishGreetingRepositoryImpl();
    }
    @Profile("EN")
    @Bean
    I18nEnglishGreetingService i18nService(EnglishGreetingRepository englishGreetingRepository) {
        return new I18nEnglishGreetingService(englishGreetingRepository);
    }

    @Primary
    @Bean
    PrimaryGreetingService primaryGreetingService() {
        return new PrimaryGreetingService();
    }

    //Configured bringing constructor bean into the context in XML
//    @Bean
//    ConstructorGreetingService constructorGreetingService() {
//        return new ConstructorGreetingService();
//    }

    @Bean
    PropertyInjectedGreetingService propertyInjectedGreetingService() {
        return new PropertyInjectedGreetingService();
    }

    @Bean
    SetterInjectedGreetingService setterInjectedGreetingService() {
        return new SetterInjectedGreetingService();
    }
}
