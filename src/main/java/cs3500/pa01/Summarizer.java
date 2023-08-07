package cs3500.pa01;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents a system that summarizes a list of .md (Markdown) files, outputting the resulting
 * summary (to serve as a study guide) into a new .md file
 */
public class Summarizer {

  /**
   * Summarizes a given list of MarkdownFiles, scraping their headers and important information
   * (denoted by double square brackets, [[*info*]]), and outputs the summaries into a single
   * study guide file at the given filepath
   *
   * @param input the ArrayList of MarkdownFiles to be summarized
   * @param output the filepath at which to create a new study guide, consisting of the summaries
   *               of the input files
   */
  public void summarizeFiles(ArrayList<MarkdownFile> input, Path output) {

    // create a StringBuilder to store the contents of the summaries of all input files
    StringBuilder contents = new StringBuilder();

    // append the summary of each input file to contents
    for (MarkdownFile file : input) {
      contents.append(this.summarizeFile(file, contents));
    }

    // Convert String to data for writing ("raw" byte data)
    byte[] data = contents.toString().getBytes();

    // The path may not exist, or we may not have permissions to write to it,
    // in which case we need to handle that error (hence try-catch)
    try {
      Files.write(output, data);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Summarizes the important information of a given MarkdownFile to later add to the
   * given study guide contents
   *
   * @param file MarkdownFile that gets summarized
   * @param contents StringBuilder containing the study guide content to which to add the summary
   *                 of this MarkdownFile
   * @return the summary of this MarkdownFile as a StringBuilder object
   */
  private StringBuilder summarizeFile(MarkdownFile file, StringBuilder contents) {
    // get the path of the given file
    Path p = file.getPath();

    // Initialize a Scanner to read the given file
    Scanner sc;
    // The file may not exist, in which case we need to handle that error
    try {
      sc = new Scanner(new FileInputStream(p.toFile()));
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }

    // create a StringBuilder to store a summary of the current file
    StringBuilder summary = new StringBuilder();

    // while the file has a next line to scan
    while (sc.hasNextLine()) {

      // store the contents of the next line in a new StringBuilder
      StringBuilder currentLine = new StringBuilder(sc.nextLine());

      // if the line starts with a #, it is a header
      if (currentLine.indexOf("#") == 0) {
        // , so, store the entire line in the summary
        this.getHeader(summary, currentLine, contents);
      } else { // otherwise, the line is not a header
        // so, store all of this line's important info
        this.getImportantInfo(sc, summary, currentLine, false);
      }
    }

    return summary;
  }

  /**
   * Scrapes the content of a header in a Markdown file.
   *
   * @param summary the summary of the current file so far
   * @param currentLine the current line being scanned for information
   * @param contents the contents of the study guide file so far
   */
  private void getHeader(StringBuilder summary, StringBuilder currentLine, StringBuilder contents) {
    // if summary and contents are both not empty, add the header without a new line before it
    if (summary.isEmpty() && contents.isEmpty()) {
      summary.append(currentLine).append("\n");
    } else { // otherwise, add the header with a new line before it
      summary.append("\n").append(currentLine).append("\n");
    }
  }

  /**
   * Scrapes the content of the important information (denoted with square brackets)
   * in a Markdown file
   *
   * @param sc Scanner that scans each line of a Markdown file
   * @param summary the summary of the current file so far
   * @param currentLine the current line being scanned for information
   * @param inBrackets a boolean representing whether the current text in
   *                   the line being scanned is within square brackets (and is thus important)
   */
  private void getImportantInfo(Scanner sc, StringBuilder summary,
                                StringBuilder currentLine, boolean inBrackets) {
    // if you're currently not within brackets, check for open brackets
    if (!inBrackets) {
      // get the index of the open brackets (-1 if they don't exist)
      int open = currentLine.indexOf("[[");
      // if the current line contains open brackets, call this method on everything after
      // the open brackets; otherwise, do nothing
      if (open != -1) {
        this.getImportantInfo(sc, summary.append("- "), currentLine.delete(0, open + 2), true);
      }
    } else { // else, you're currently within brackets (not yet found closing brackets)
      // get the index of the closing brackets (-1 if they don't exist)
      int close = currentLine.indexOf("]]");

      // if current line doesn't contain closing brackets...
      if (close == -1) {
        // append the whole line to the summary
        summary.append(currentLine);

        // call on the next line (guaranteed to exist, based on Assumption #1)
        this.getImportantInfo(sc, summary, new StringBuilder(sc.nextLine()), true);
      } else { // otherwise, the closing brackets in the current line, so...
        // add everything before the closed brackets to the summary
        summary.append(currentLine.substring(0, close)).append("\n");

        // store the rest of the line, and if it has more open brackets, get the important info
        // from it
        StringBuilder restOfLine = new StringBuilder(currentLine.substring(close));
        if (restOfLine.indexOf("[[") != -1) {
          this.getImportantInfo(sc, summary, restOfLine, false);
        }
      }
    }
  }

}
