package com.example.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@Scope("prototype")
public class ContadorBean {

    private static int instances = 0;
    private final int id;
    private int counter = 0;

    public ContadorBean() {
        id = ++instances;
    }

    @PostConstruct
    public void init() {
        System.out.println("ContadorBean init, id=" + id);
    }

    public int getNext() { 
        return ++counter; 
    }
    
    @Override
    public String toString() {
        return "ContadorBean#" + id + " counter: " + counter;
    }
}
