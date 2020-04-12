package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String inputPath = "";
        String outputPath = "";
        int sec = (60*0)+8;
        List<SubTitle> subTitles = readInputFile(inputPath);
        timeAdder(subTitles,sec);
        writeOutput(subTitles, outputPath);
    }

    private static void writeOutput(List<SubTitle> subTitles, String outputPath) {
        try {
            FileWriter myWriter = new FileWriter(outputPath);
            System.out.println("File Writing started...");
            for (SubTitle subTitle : subTitles) {
                myWriter.write(String.valueOf(subTitle.getLineNo()));
                myWriter.write("\n");
                myWriter.write(subTitle.getStartTime() + " " + SubTitle.arrow + " " + subTitle.getEndTime());
                myWriter.write("\n");
                myWriter.write(subTitle.getSubtitle());
                myWriter.write("\n\n");
            }

            myWriter.close();
            System.out.println("Successfully wrote to the file...");

        } catch (IOException e) {
            System.out.println("An error occurred while file write...");
            e.printStackTrace();
        }
    }

    private static void timeAdder(List<SubTitle> subTitles, int sec) {
        subTitles.forEach(subTitle -> {
            subTitle.setStartTime(subTitle.getStartTime().plusSeconds(sec));
            subTitle.setEndTime(subTitle.getEndTime().plusSeconds(sec));
        });
    }

    private static List<SubTitle> readInputFile(String inputPath) {
        List<SubTitle> subTitles = new ArrayList<>();

        try {
            File file = new File(inputPath);
            System.out.println("Starting file Read ...");
            Scanner myReader = new Scanner(file);
            SubTitle subTitle = new SubTitle();
            String data;
            int i = 0;
            boolean init = true;
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();

                if (data.trim().equals("")) {
                    i = 0;
                } else if (i == 0) {
                    if (!init) {
                        subTitles.add(subTitle);
                    }
                    if (init) {
                        init = false;
                    }

                    subTitle = new SubTitle();
                    subTitle.setSubtitle("");
                    subTitle.setLineNo(Integer.parseInt(data));
                    i++;
                } else if (i == 1) {
                    String[] split = data.split(SubTitle.arrow);
                    subTitle.setStartTime(LocalTime.parse(split[0].trim().replace(",", ".")));
                    subTitle.setEndTime(LocalTime.parse(split[1].trim().replace(",", ".")));
                    i++;
                } else {
                    if(!subTitle.getSubtitle().trim().equals(""))
                    {
                        subTitle.setSubtitle(subTitle.getSubtitle()+"\n");
                    }
                    subTitle.setSubtitle(subTitle.getSubtitle() + data);
                }
            }
            System.out.println("File Read Complete ...");
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred in  file read...");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("An error occurred in subtitle No conversion...");
            e.printStackTrace();
        }
        return subTitles;
    }
}
