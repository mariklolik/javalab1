package com.javalabs;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.EnumSet;


public class Main {
    static JDUPromptData promptArgs = new JDUPromptData();
    public static void main(String[] args) throws IOException {
        try {
            promptArgs = new JDUPromptData(args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        Path start = Paths.get((String) promptArgs.getValue("dir"));

        FileVisitor visitor = new FileVisitor((String) promptArgs.getValue("dir"), 0, (Boolean) promptArgs.getValue("symlinks"));
        EnumSet<FileVisitOption> opts;
        if ((Boolean) promptArgs.getValue("symlinks")) {
            opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
        } else {
            opts = EnumSet.noneOf(FileVisitOption.class);
        }
        Integer depth = (Integer) promptArgs.getValue("depth");




        Files.walkFileTree(start, opts, depth, visitor);


        while (!visitor.printStack.isEmpty()) {
            String elem = visitor.printStack.pop();
            System.out.println(elem);
        }



    }
}