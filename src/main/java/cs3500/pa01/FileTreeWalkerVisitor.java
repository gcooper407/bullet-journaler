package cs3500.pa01;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

/**
 * Traverses a directory (tree), storing .md files (as MarkdownFile objects) in an ordered
 * list (based on a user-specified ordering flag).
 */
public class FileTreeWalkerVisitor implements FileVisitor<Path> {

  private ArrayList<MarkdownFile> files;
  private boolean seen;

  /**
   * Instantiates a file visitor with an empty list of Markdown files and
   * the directory not yet seen.
   */
  public FileTreeWalkerVisitor() {
    this.files = new ArrayList<>();
    this.seen = false;
  }

  /**
   * Instantiates a file visitor with a pre-set list of Markdown files and
   * the directory already marked as seen.
   *
   * @param files list of Markdown files
   */
  public FileTreeWalkerVisitor(ArrayList<MarkdownFile> files) {
    this.files = files;
    this.seen = true;
  }

  /**
   * Gets the files collected by this FileTreeWalkerVisitor, in a user-specified order.
   *
   * @param flag a user-specified way to order the collected files
   * @return an ArrayList of MarkdownFiles, ordered according to the given ordering flag, either
   *         "filename" to order by file name, "created" to order by time created, and "modified" to
   *         order by time last modified.
   */
  public ArrayList<MarkdownFile> getOrderedFiles(String flag) {
    if (!this.seen) {
      throw new IllegalStateException("Unable to get files before calling the "
          + "FileVisitor callback methods.");
    }

    // Note: asked TA Noam if I need to use Enum for flags instead of String, but he said that
    // because of the straight-forward, non-complex way I've structured my implementation,
    // using an Enum would be redundant and is thus not needed.
    if (flag.equals("filename")) {
      this.files.sort((f1, f2) -> f1.getName().compareTo(f2.getName()));
    } else if (flag.equals("created")) {
      this.files.sort((f1, f2) -> f1.getCreated().compareTo(f2.getCreated()));
    } else if (flag.equals("modified")) {
      this.files.sort((f1, f2) -> f1.getModified().compareTo(f2.getModified()));
    } else {
      throw new IllegalArgumentException("Invalid flag, must be one of: "
          + "'filename', 'created', or 'modified'");
    }

    return this.files;
  }

  /**
   * Sets the value of the 'seen' boolean field to the given boolean value (used for testing).
   *
   * @param val the boolean value to set the 'seen' field to
   */
  public void setSeen(boolean val) {
    this.seen = val;
  }

  /**
   * Gets the value of the 'seen' boolean field
   *
   * @return the value of the 'seen' boolean field
   */
  public boolean getSeen() {
    return this.seen;
  }


  /**
   * Invoked for a file in a directory.
   *
   * @param file a reference to the file
   * @param attr the file's basic attributes
   * @return the visit result
   */
  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
    if (attr.isRegularFile() && file.toString().endsWith(".md")) {
      this.files.add(new MarkdownFile(file, attr));
    }
    return FileVisitResult.CONTINUE;
  }


  /**
   * Invoked for a file that could not be visited.
   *
   * @param file a reference to the file
   * @param exc  the I/O exception that prevented the file from being visited
   * @return the visit result
   */
  @Override
  public FileVisitResult visitFileFailed(Path file, IOException exc) {
    System.err.println(exc);
    return FileVisitResult.CONTINUE;
  }

  /**
   * Invoked for a directory before entries in the directory are visited.
   *
   * @param dir a reference to the directory
   * @param attr the directory's basic attributes
   *
   * @return the visit result
   */
  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attr) {
    this.seen = true;
    return FileVisitResult.CONTINUE;
  }


  /**
   * Invoked for a directory after entries in the directory, and all of their descendants,
   * have been visited.
   *
   * @param dir a reference to the directory
   * @param exc null if the iteration of the directory completes without
   *            an error; otherwise the I/O exception that caused the iteration
   *            of the directory to complete prematurely
   * @return the visit result
   */
  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
    return FileVisitResult.CONTINUE;
  }

}
