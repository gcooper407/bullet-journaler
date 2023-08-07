package cs3500.pa01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the elements (constructors and methods) of the MarkdownFile class
 */
public class MarkdownFileTest {

  Path arrayPath;
  BasicFileAttributes arrayAttr;
  MarkdownFile arrays;

  Path vectorPath;
  BasicFileAttributes vectorAttr;
  MarkdownFile vectors;

  Path fakePath;
  MarkdownFile fakeFile;

  String other;

  /**
   * Initializes the fields of this tester class.
   */
  @BeforeEach
  public void initMdFile() {
    this.arrayPath = new File("src/test/resources/Examples/arrays.md").toPath();
    try {
      this.arrayAttr = Files.readAttributes(arrayPath, BasicFileAttributes.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    this.arrays = new MarkdownFile(arrayPath, arrayAttr);


    this.vectorPath = new File("src/test/resources/Examples/arrays.md").toPath();
    try {
      this.vectorAttr = Files.readAttributes(arrayPath, BasicFileAttributes.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    this.vectors = new MarkdownFile(vectorPath, vectorAttr);

    this.fakePath = Path.of("src/test/resources/Examples/fake.txt");
    this.fakeFile = new MarkdownFile(fakePath, vectorAttr);

    this.other = "other object";

  }

  /**
   * Tests the getName() method on the MarkdownFile class.
   */
  @Test
  public void getName() {
    assertEquals("arrays.md", this.arrays.getName());
  }

  /**
   * Tests the getCreated() method on the MarkdownFile class.
   */
  @Test
  public void getCreated() {
    assertEquals(this.arrayAttr.creationTime(), this.arrays.getCreated());
  }

  /**
   * Tests the getModified() method on the MarkdownFile class.
   */
  @Test
  public void getModified() {
    assertEquals(this.arrayAttr.lastModifiedTime(), this.arrays.getModified());
  }

  /**
   * Tests the getPath() method on the MarkdownFile class.
   */
  @Test
  public void getPath() {
    assertEquals(this.arrayPath, this.arrays.getPath());
  }

  /**
   * Tests the equals(Object other) method on the MarkdownFile class.
   */
  @Test
  public void equals() {
    assertTrue(this.arrays.equals(this.arrays));
    assertFalse(this.arrays.equals(this.vectors));
    assertFalse(this.arrays.equals(this.fakeFile));
    assertFalse(this.arrays.equals(this.other));
  }

  /**
   * Tests the hashCode() method on the MarkdownFile class
   */
  @Test
  public void hashCodeTest() {
    assertEquals(this.arrays.hashCode(), this.arrays.hashCode());
    assertNotEquals(this.arrays.hashCode(), this.vectors.hashCode());
  }
}