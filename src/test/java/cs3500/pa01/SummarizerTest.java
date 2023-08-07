package cs3500.pa01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *  Tests the elements (constructors and methods) of the Summarizer class, which also
 *  serves to test all related helper methods.
 */
public class SummarizerTest {

  Summarizer summarizer;

  Path arrayPath;
  BasicFileAttributes arrayAttr;
  MarkdownFile arrays;

  Path vectorPath;
  BasicFileAttributes vectorAttr;
  MarkdownFile vectors;

  Path fakePath;
  MarkdownFile fakeFile;

  ArrayList<MarkdownFile> fileList = new ArrayList<>();

  Path outPath;

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

  /**
   * Initializes the fields of this tester class.
   */
  @BeforeEach
  public void initMdFile() {
    this.summarizer = new Summarizer();

    this.arrayPath = new File("src/test/resources/Examples/arrays.md").toPath();
    try {
      this.arrayAttr = Files.readAttributes(arrayPath, BasicFileAttributes.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    this.arrays = new MarkdownFile(arrayPath, arrayAttr);

    this.vectorPath = new File("src/test/resources/Examples/vectors.md").toPath();
    try {
      this.vectorAttr = Files.readAttributes(vectorPath, BasicFileAttributes.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    this.vectors = new MarkdownFile(vectorPath, vectorAttr);

    this.fakePath = Path.of("src/test/resources/FakeDirectory/fakeFile.md");
    this.fakeFile = new MarkdownFile(fakePath, vectorAttr);

    this.fileList = new ArrayList<>();
    this.fileList.add(this.arrays);
    this.fileList.add(this.vectors);

    this.outPath = new File("src/test/resources/summarizeOutput.md").toPath();
  }

  /**
   * Deletes the output path that the Summarizer class writes to in this class's test,
   * to test certain functionality (specifically regarding Summarizer's ability to
   * create a new file).
   */
  @AfterEach
  public void deleteOutFile() {
    try {
      Files.deleteIfExists(this.outPath);
    } catch (IOException e) {
      fail();
    }
  }

  /**
   * Tests the summarize() method on the Summarizer class; specifically, tests
   * that summarize() properly creates a new file with the proper content
   * when the file defined by the output path doesn't already exist.
   */
  @Test
  public void summarizeCreates() {
    assertFalse(Files.exists(this.outPath));
    summarizer.summarizeFiles(this.fileList, this.outPath);
    assertTrue(Files.exists(this.outPath));

    try {
      assertEquals(this.expected, Files.readString(this.outPath));
    } catch (IOException e) {
      fail();
    }

    assertThrows(RuntimeException.class, () ->
        summarizer.summarizeFiles(this.fileList,
            new File("src/test/fakeDirectory/summarizeOutput.md").toPath()));
  }

  /**
   * Tests the summarize() method on the Summarizer class; specifically, tests
   * that summarize() properly overwrites an existing file with the proper content
   * when the file defined by the output path already exists.
   */
  @Test
  public void summarizeOverwrites() {

    byte[] bytes = "nothing here".getBytes();

    try {
      Files.write(this.outPath, bytes);
      assertNotEquals(this.expected, Files.readString(this.outPath));
    } catch (IOException e) {
      fail();
    }

    summarizer.summarizeFiles(this.fileList, this.outPath);


    try {
      assertEquals(expected, Files.readString(this.outPath));
    } catch (IOException e) {
      fail();
    }

  }


  /**
   * Tests that the summarize() method fails when a nonexistent file is passed in
   * as an element of the input list.
   */
  @Test
  public void summarizeFails() {
    this.fileList.add(this.fakeFile);

    assertThrows(RuntimeException.class,
        () -> summarizer.summarizeFiles(this.fileList, this.outPath));
  }

}