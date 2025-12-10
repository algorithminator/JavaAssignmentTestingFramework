package assignment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junitpioneer.jupiter.DisableIfTestFails;
import assignment.testing.framework.BaseAssignment;

import static org.junit.jupiter.api.Assertions.*;
import static assignment.testing.framework.Utilities.*;

import java.lang.reflect.AccessFlag;
import java.util.List;


@DisableIfTestFails
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Assignment Title")
public class TestAssignment extends BaseAssignment
{
    @DisplayName("Assignment Task #01")
    @Order(1)
    @Test()
    public void task01() {
        // Code that tests the assignment goes here
    }
}
