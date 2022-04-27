package edu.rpi.legup.grader;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.save.InvalidFileFormatException;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LegupGrader
{
    /**
     * Checks the proof for all files
     */
    public static void checkProofAll(GameBoard facade) {
        JFileChooser folderBrowser = new JFileChooser();
        folderBrowser.setCurrentDirectory(new java.io.File("."));
        folderBrowser.setDialogTitle("Select Directory");
        folderBrowser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        folderBrowser.setAcceptAllFileFilterUsed(false);
        folderBrowser.showOpenDialog(this);
        File folder = folderBrowser.getSelectedFile();

        //FileWriter csvWriter = new FileWriter("new.csv");
        File resultFile = new File(folder.getAbsolutePath() + File.separator +"result.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile))) {
            writer.append("Name");
            writer.append(",");
            writer.append("File Name");
            writer.append(",");
            writer.append("Solved or not");
            writer.append("\n");
            //csvWriter.flush();
            //csvWriter.close();
            for (final File folderEntry : folder.listFiles(File::isDirectory)) {
                writer.append(folderEntry.getName());
                writer.append(",");
                int count1 = 0;
                System.out.println("FILES: " + folderEntry.listFiles());
                for (final File fileEntry : folderEntry.listFiles()) {
                    if (fileEntry.getName().charAt(0) == '.'){
                        continue;
                    }
                    count1++;
                    if (count1 > 1){
                        writer.append(folderEntry.getName());
                        writer.append(",");
                    }
                    writer.append(fileEntry.getName());
                    writer.append(",");
                    String fileName = folderEntry.getAbsolutePath() + File.separator + fileEntry.getName();
                    File puzzleFile = new File(fileName);
                    if (puzzleFile != null && puzzleFile.exists()) {
                        try {
                            GameBoardFacade.getInstance().loadPuzzle(fileName);
                            String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
                            setTitle(puzzleName + " - " + puzzleFile.getName());
                            facade = GameBoardFacade.getInstance();
                            Puzzle puzzle = facade.getPuzzleModule();
                            if (puzzle.isPuzzleComplete()) {
                                writer.append("1, Solved");
                                System.out.println(fileEntry.getName() + "  solved");
                            } else {
                                writer.append("0, Unsolved");
                                System.out.println(fileEntry.getName() + "  not solved");
                            }
                            writer.append("\n");
                        } catch (InvalidFileFormatException e) {
                            LOGGER.error(e.getMessage());
                            writer.append("?, Ungradeable");
                            writer.append("\n");
                        }
                    }
                }
                if (count1 == 0){
                    writer.append("No file");
                    writer.append("\n");
                }
            }
        }catch (IOException ex){
//            System.out.println("ERROR: " +ex.getMessage());
            LOGGER.error(ex.getMessage());
        }

        JOptionPane.showMessageDialog(null, "Batch grading complete.");

        /*fileDialog.setMode(FileDialog.LOAD);
        fileDialog.setTitle("Select Puzzle");
        fileDialog.setVisible(true);
        String fileName = null;
        File puzzleFile = null;
        if (fileDialog.getDirectory() != null && fileDialog.getFile() != null) {
            fileName = fileDialog.getDirectory() + File.separator + fileDialog.getFile();
            puzzleFile = new File(fileName);
        }

        if (puzzleFile != null && puzzleFile.exists()) {
            try {
                GameBoardFacade.getInstance().loadPuzzle(fileName);
                String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
                setTitle(puzzleName + " - " + puzzleFile.getName());
            } catch (InvalidFileFormatException e) {
                LOGGER.error(e.getMessage());
            }
        }*/
    }
}
