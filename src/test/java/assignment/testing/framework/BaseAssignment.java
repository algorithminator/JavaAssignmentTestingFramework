package assignment.testing.framework;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseAssignment
{
    @BeforeEach
    public void setUp() {
        Utilities.setStandardOutput();
    }

    @AfterEach
    public void tearDown() {
        Utilities.resetStandardOutput();
    }
}
