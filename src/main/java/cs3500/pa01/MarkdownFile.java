package cs3500.pa01;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

/**
 * Represents a Markdown (.md) file with a filepath and basic file attributes
 */
public class MarkdownFile {

  private Path file;
  private BasicFileAttributes attrs;

  /**
   * Instantiates a Markdown file with a filepath and basic attributes
   *
   * @param file filepath that represents the location of the Markdown file
   * @param attrs basic attributes of the Markdown file
   */
  public MarkdownFile(Path file, BasicFileAttributes attrs) {
    this.file = file;
    this.attrs = attrs;
  }

  /**
   * Gets the name of this MarkdownFile
   *
   * @return the name of this MarkdownFile as a String
   */
  public String getName() {
    return this.file.toFile().getName();
  }

  /**
   * Gets the time at which this MarkdownFile was created
   *
   * @return the time at which this MarkdownFile was created, as a FileTime object
   */
  public FileTime getCreated() {
    return this.attrs.creationTime();
  }

  /**
   * Gets the time at which this MarkdownFile was last modified
   *
   * @return the time at which this MarkdownFile was last modified, as a FileTime object
   */
  public FileTime getModified() {
    return this.attrs.lastModifiedTime();
  }

  /**
   * Gets the filepath of this MarkdownFile
   *
   * @return the filepath of this MarkdownFile, as a Path object
   */
  public Path getPath() {
    return this.file;
  }

  /**
   * Indicates whether some other object is "equal to" this MarkdownFile object.
   *
   * @param other the reference object with which to compare.
   * @return true if this MarkdownFile object is the same as the other argument; false otherwise.
   */
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof MarkdownFile)) {
      return false;
    }

    MarkdownFile that = (MarkdownFile) other;
    return this.file.equals(that.file) && this.attrs.equals(that.attrs);
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for this object.
   */
  @Override
  public int hashCode() {
    return this.file.hashCode() * 10000 + this.attrs.hashCode();
  }

}
