package com.example.aop;

import java.util.Optional;

import org.jspecify.annotations.NonNull;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

public class DummyService {
    private String value = null;
    @Autowired @Lazy
    private DummyService self;
    
    public Optional<String> getValue() {
        return Optional.ofNullable(value);
    }

    public void setValue(@NonNull String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("No acepto argumentos nulos");
        }
        this.value = value;
    }
    
    public void clearValue() {
        self.privado();
        value = alwaysNull(); // auto referenciado
//      value = ((DummyService) AopContext.currentProxy()).alwaysNull();
//    	value =  self.alwaysNull();
    }

    @NonNull 
    public String echo(String input) {
        return input;
    }
    
    public String alwaysNull() {
        return null;
    }

    private void privado() {}
}