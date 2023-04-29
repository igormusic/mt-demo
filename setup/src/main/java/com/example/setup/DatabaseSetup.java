package com.example.setup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

@Component
@Slf4j
public class DatabaseSetup implements ApplicationRunner {

    private final EntityManagerFactory entityManagerFactory;

    public DatabaseSetup(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // check if database messages exists

        var entityManager = entityManagerFactory.createEntityManager();
        var count = (Integer) entityManager.createNativeQuery("select count(*) from sysdatabases where name = 'payments'").getSingleResult();
        EntityTransaction tx;

        if(count==0){
            tx = entityManager.getTransaction();
            tx.begin();

            entityManager.createNativeQuery("create database payments").executeUpdate();

            tx.commit();

            log.info("Database payments created");
        }
        else {
            log.info("Database payments already exists");
        }

        tx = entityManager.getTransaction();
        tx.begin();
        entityManager.createNativeQuery("USE payments").executeUpdate();
        tx.commit();

        count = (Integer) entityManager
                .createNativeQuery("SELECT count(*) FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = 'messages'")
                .getSingleResult();

        if(count==0){
            tx = entityManager.getTransaction();
            tx.begin();

            entityManager.createNativeQuery("create schema messages").executeUpdate();

            tx.commit();

            log.info("Schema messages created");
        }
        else {
            log.info("Schema messages already exists");
        }

    }
}
