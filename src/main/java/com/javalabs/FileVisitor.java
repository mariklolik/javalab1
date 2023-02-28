package com.javalabs;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.Stack;

import static com.javalabs.Main.promptArgs;

public class FileVisitor implements java.nio.file.FileVisitor<Path> {
    public Stack<String> printStack;
    private final Stack<Pair<Path, Long>> stck;
    private final String startPath;
    private final Integer startTabs;
    private Long lastDirSize;

    private Integer curDiff;

    private final Boolean visitSymlinks;

    private Integer countSubString(String charToCount, String src) {
        return (src.length() - src.replace(charToCount, "").length());
    }

    private Integer diff(String currentPath) {
        return countSubString("\\", currentPath.substring(startPath.length()));
    }


    FileVisitor(String startpath, Integer startTabs, Boolean visitSymlinks ) {
        this.startPath = startpath;
        this.stck = new Stack<>();
        this.printStack = new Stack<>();
        this.lastDirSize = 0L;
        this.startTabs = startTabs;
        this.visitSymlinks = visitSymlinks;
    }


    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {

        if (!dir.toRealPath().toString().equals(dir.toString())) {
            if (visitSymlinks) {
                EnumSet<FileVisitOption> opts;
                if ((Boolean) promptArgs.getValue("symlinks")) {
                    opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
                } else {
                    opts = EnumSet.noneOf(FileVisitOption.class);
                }
                Integer depth = (Integer) promptArgs.getValue("depth");
                curDiff = diff(dir.toString());
                FileVisitor visitor = new FileVisitor(dir.toRealPath().toString(), curDiff, (Boolean) promptArgs.getValue("symlinks"));

                Files.walkFileTree(dir.toRealPath(), opts, Math.max(0, depth - curDiff), visitor);
                this.printStack.addAll(visitor.printStack);

            }
            return FileVisitResult.SKIP_SUBTREE;
        }

        for (var x : stck) {
            x.param2 += lastDirSize;
        }
        stck.push(new Pair<>(dir, 0L));
        curDiff = diff(dir.toString());

        lastDirSize = 0L;
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        printStack.push("\t".repeat(curDiff + 1 + startTabs) + "📄" + Paths.get(startPath).relativize(file) + "[" + Files.size(file) + "bytes]");

        lastDirSize += attrs.size();
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {

        if (dir.toRealPath().toString().equals(dir.toString())) {
            for (var x : stck) {
                x.param2 += lastDirSize;
            }
        }


        if (curDiff < 0) {
            curDiff = diff(dir.toString());
        }
        printStack.push("\t".repeat(curDiff + startTabs) + "📁" + Paths.get(startPath).relativize(dir) + "[" + stck.pop().param2 + "bytes]");
        lastDirSize = 0L;
        curDiff -= 1;
        return FileVisitResult.CONTINUE;
    }
}
