package com.example.aop;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(DummyService.class)
//@EnableAspectJAutoProxy(exposeProxy = true)
class StrictNullChecksAspectTest {
    @Autowired
    DummyService service;
 
    @Test
    @DisplayName("Valores validos para la propiedad Value")
    void testValueOK() {
        assertDoesNotThrow(() -> service.setValue("valor"));
        assertTrue(service.getValue().isPresent());
        assertEquals("valor", service.getValue().get());
    }
   
    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Valores invalidos para la propiedad Value")
    void testValueKO(String caso) {
        var ex = assertThrows(IllegalArgumentException.class, () -> service.setValue(caso));
        assertTrue(service.getValue().isEmpty());
    }

    @Test
    @DisplayName("Borrar (poner a null) la propiedad Value")
    void testClearValue() {
        assertDoesNotThrow(() -> service.setValue("not null"));
        assertTrue(service.getValue().isPresent());
        assertDoesNotThrow(() -> service.clearValue());
        assertNotNull(service.getValue());
        assertTrue(service.getValue().isEmpty());
    }


    @ParameterizedTest
    @ValueSource(strings = {"not null"})
    @EmptySource
    @DisplayName("Valores validos para los argumentos de los métodos")
    void testValidArgumentAndReturn(String caso) {
        assertEquals("not null", service.echo("not null"));
    }

    @Test
    @DisplayName("El aspecto lanza IllegalArgumentException con argumentos nulos")
    void testNullArgumentThrows() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.echo(null));
        assertTrue(ex.getMessage().contains("Illegal argument"));
    }

    @Test
    @DisplayName("El aspecto lanza NoSuchElementException con retornos nulos")
    void testNullReturnThrows() {
        Exception ex = assertThrows(java.util.NoSuchElementException.class, () -> service.alwaysNull());
        assertTrue(ex.getMessage().contains("Returns null"));
    }

}