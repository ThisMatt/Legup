package edu.rpi.legup.grader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LegupGrader
{
    public void checkProofAll(File folder) throws IOException
    {
        String filename = java.time.LocalDate.now() + "_legup_grading_results";
        final String EXTENSION = ".csv";
        final String ABSOLUTE_PATH = folder.getAbsolutePath();
        if (isExistingFile(ABSOLUTE_PATH, filename, EXTENSION))
            filename = this.getValidFilename(ABSOLUTE_PATH, filename, EXTENSION);

        final String[] HEADERS = {"Name", "File Name", "Problem Status Number", "Problem Status"};
        File resultFile = new File(ABSOLUTE_PATH + File.separator + filename + EXTENSION);
        BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile));

        // Create header
        this.createCSVHeader(writer);

        for (final File folderEntry : folder.listFiles(File::isDirectory))
        {
            System.out.println(folderEntry.getName());
        }
        writer.close();
    }

    public void checkProof()
    {

    }

    private void createCSVHeader(BufferedWriter writer) throws IOException
    {
        writer.append("Name");
        writer.append(",");
        writer.append("File Name");
        writer.append(",");
        writer.append("Problem Status Number");
        writer.append(",");
        writer.append("Problem Status");
        writer.append("\n");
    }

    private boolean isExistingFile(String folderAbsolutePath, String filename, String extension)
    {
        File tempFile = new File(folderAbsolutePath + File.separator + filename + extension);
        return tempFile.exists();
    }

    private String getValidFilename(String folderAbsolutePath, String filename, String extension)
    {
        int counter = 1;
        String newFilename = filename + counter;
        while (isExistingFile(folderAbsolutePath, newFilename, extension))
            counter++;
        return newFilename;
    }

    /**
     * Checks the proof for all files
     */
    /*private static void oldCheckProofAll(File folder)
    {
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
                            // LOGGER.error(e.getMessage());
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
        }
    }*/
}
