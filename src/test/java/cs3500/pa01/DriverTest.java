package cs3500.pa01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/**
 * Tests the elements (constructors and methods) of the Driver class.
 */
public class DriverTest {

  /**
   * Tests the main method of the Driver class.
   */
  @Test
  public void mainTest() {
    String[] args = {"src/test/resources/Examples/",
        "filename",
        "src/test/resources/driverOutput.md"};
    String[] argsFail = {"src/test/resources/FakeDirectory/",
        "filename",
        "src/test/resources/driverOutput.md"};

    // tests (implicit) constructor
    Driver d = new Driver();

    assertThrows(RuntimeException.class, () -> Driver.main(argsFail));

    // implicitly tests that main runs properly when given valid inputs
    Driver.main(args);

    String expected = """
        # Java Arrays
        - An **array** is a collection of variables of the same type
        
        ## Declaring an Array
        - General Form: type[] arrayName;
        - only creates a reference
        - no array has actually been created yet
        
        ## Creating an Array (Instantiation)
        - General form:  arrayName = new type[numberOfElements];
        - numberOfElements must be a positive Integer.
        - Gotcha: Array size is not modifiable once instantiated.
        
        # Vectors
        - Vectors act like resizable arrays
        
        ## Declaring a vector
        - General Form: Vector<type> v = new Vector();
        - type needs to be a valid reference type
        
        ## Adding an element to a vector
        - v.add(object of type);
        """;

    // explicitly tests that the inputs produce some expected output, located
    // in a test file
    try {
      assertEquals(expected,
          Files.readString(Path.of("src/test/resources/driverOutput.md")));
    } catch (IOException e) {
      fail();
    }
  }

}