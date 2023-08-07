package cs3500.pa01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the elements (constructors and methods) of the FileTreeWalkerVisitor class
 */
public class FileTreeWalkerVisitorTest {

  FileTreeWalkerVisitor pf = new FileTreeWalkerVisitor();

  Path vectorPath;
  BasicFileAttributes vectorAttr;
  MarkdownFile vectors;

  Path arrayPath;
  BasicFileAttributes arrayAttr;
  MarkdownFile arrays;

  Path extraPath;
  BasicFileAttributes extraAttr;
  MarkdownFile extra;

  ArrayList<MarkdownFile> fileList = new ArrayList<>();

  /**
   * Initializes the fields of this tester class.
   */
  @BeforeEach
  public void initMdFile()  {

    try {
      File v = Path.of("src/test/resources/Examples/vectors.md").toFile();
      v.setLastModified(Instant.parse("2023-04-03T14:01:00Z").toEpochMilli());
      this.vectorPath = v.toPath();
      this.vectorAttr = Files.readAttributes(vectorPath, BasicFileAttributes.class);
    } catch (IOException e) {
      fail();
    }

    this.vectors = new MarkdownFile(vectorPath, vectorAttr);


    try {
      File a = Path.of("src/test/resources/Examples/arrays.md").toFile();
      a.setLastModified(Instant.parse("2023-04-04T11:37:00Z").toEpochMilli());
      this.arrayPath = a.toPath();
      this.arrayAttr = Files.readAttributes(arrayPath, BasicFileAttributes.class);
    } catch (IOException e) {
      fail();
    }

    this.arrays = new MarkdownFile(arrayPath, arrayAttr);

    try {
      this.extraPath = Path.of("src/test/resources/Examples/extra.md");
      this.extraAttr = Files.readAttributes(extraPath, BasicFileAttributes.class);
    } catch (IOException e) {
      fail();
    }

    this.extra = new MarkdownFile(extraPath, extraAttr);

    this.fileList = new ArrayList<>();
    this.fileList.add(this.arrays);
    this.fileList.add(this.vectors);

    this.pf = new FileTreeWalkerVisitor(this.fileList);
  }

  /**
   * Tests the getFiles() method on the FileTreeWalkerVisitor class.
   */
  @Test
  public void getFiles() {

    ArrayList<MarkdownFile> resultFilename = new ArrayList<>(pf.getOrderedFiles("filename"));
    assertEquals(2, resultFilename.size());
    assertEquals(this.arrays, resultFilename.get(0));
    assertEquals(this.vectors, resultFilename.get(1));

    ArrayList<MarkdownFile> resultCreated = new ArrayList<>(pf.getOrderedFiles("created"));
    assertEquals(2, resultCreated.size());
    assertEquals(this.vectors, resultCreated.get(0));
    assertEquals(this.arrays, resultCreated.get(1));

    ArrayList<MarkdownFile> resultModified = new ArrayList<>(pf.getOrderedFiles("modified"));
    assertEquals(2, resultModified.size());
    assertEquals(this.vectors, resultModified.get(0));
    assertEquals(this.arrays, resultModified.get(1));

    assertThrows(IllegalArgumentException.class, () -> pf.getOrderedFiles("something else"));

    pf.setSeen(false);

    assertThrows(IllegalStateException.class, () -> pf.getOrderedFiles("filename"));

  }

  /**
   * Tests the setSeen() method on the FileTreeWalkerVisitor class.
   */
  @Test
  public void setSeen() {
    assertTrue(pf.getSeen());
    pf.setSeen(false);
    assertFalse(pf.getSeen());
    pf.setSeen(true);
    assertTrue(pf.getSeen());
  }

  /**
   * Tests the visitFile() method on the FileTreeWalkerVisitor class.
   */
  @Test
  public void visitFile() {
    assertEquals(2, pf.getOrderedFiles("filename").size());
    assertEquals(FileVisitResult.CONTINUE, pf.visitFile(this.extraPath, this.extraAttr));
    assertEquals(3, pf.getOrderedFiles("filename").size());
    assertEquals(this.arrays, pf.getOrderedFiles("filename").get(0));
    assertEquals(this.extra, pf.getOrderedFiles("filename").get(1));
    assertEquals(this.vectors, pf.getOrderedFiles("filename").get(2));


    Path fake = Path.of("src/test/resources/Examples/fake.txt");

    try {
      assertEquals(FileVisitResult.CONTINUE, pf.visitFile(fake,
          Files.readAttributes(fake, BasicFileAttributes.class)));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Tests the visitFileFailed() method on the FileTreeWalkerVisitor class.
   */
  @Test
  public void visitFileFailed() {
    assertEquals(FileVisitResult.CONTINUE, pf.visitFileFailed(this.arrayPath, new IOException()));
  }

  /**
   * Tests the preVisitDirectory() method on the FileTreeWalkerVisitor class.
   */
  @Test
  public void preVisitDirectory() {
    assertEquals(FileVisitResult.CONTINUE, pf.preVisitDirectory(this.arrayPath, this.arrayAttr));
  }

  /**
   * Tests the postVisitDirectory() method on the FileTreeWalkerVisitor class.
   */
  @Test
  public void postVisitDirectory() {
    assertEquals(FileVisitResult.CONTINUE,
        pf.postVisitDirectory(this.arrayPath, new IOException()));
  }
}