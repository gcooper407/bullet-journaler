package cs3500.pa01;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * This is the main driver of this project. It contains the main method, which generates a
 * study guide based on a set of files in a user-specified input path, in some order based on
 * a user-specified ordering flag, which is output to some user-specified output path.
 *
 */
public class Driver {

  /**
   * Project entry point -- generates a study guide based on a set of files in a user-specified
   * input path, in some order based on a user-specified ordering flag, which is output
   * to some user-specified output path.
   *
   * @param args notesRoot, a string representing the directory containing markdown files to
   *             summarize; orderingFlag, a string representing how the files should be ordered
   *             in the summary; outputPath, a string representing the directory where the summary
   *             (study guide) file should be output to.
   *
   */
  public static void main(String[] args) {
    // get the root path from the user input
    Path notesRoot = Path.of(args[0]);

    // create a FileTreeWalkerVisitor
    FileTreeWalkerVisitor pf = new FileTreeWalkerVisitor();

    // Walk the file tree (if possible)
    try {
      Files.walkFileTree(notesRoot, pf);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // get the list of markdown files to summarize, based on user-inputted flag
    ArrayList<MarkdownFile> files = pf.getOrderedFiles(args[1]);

    // create a new Summarizer
    Summarizer rw = new Summarizer();

    // get the output path from user input
    Path outPath = new File(args[2]).toPath();

    // summarize the list of markdown files and store the result in the file of the output path
    rw.summarizeFiles(files, outPath);
  }

}