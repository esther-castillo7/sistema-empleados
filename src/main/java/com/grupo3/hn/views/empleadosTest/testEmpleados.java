package com.grupo3.hn.views.empleadosTest;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;

import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.testbench.TestBenchTestCase;

public class testEmpleados extends TestBenchTestCase {

    @Before
    public void setUp() {
        // Configura el controlador de Chrome
        System.setProperty("webdriver.chrome.driver", "C:\\path\\to\\chromedriver.exe"); // Cambia esto a la ruta correcta
        setDriver(new ChromeDriver());
    }

    @Test
    public void testEmpleadosView() {
        // Abre la aplicación
        getDriver().get("http://localhost:8080/empleados");

        // Verifica que la vista de empleados se cargue correctamente
        GridElement grid = $(GridElement.class).first();
        
        // Verifica que haya datos en la primera fila (ajusta según los datos esperados)
        assertTrue("La tabla de empleados no contiene datos", grid.getRowCount() > 0);
    }
}