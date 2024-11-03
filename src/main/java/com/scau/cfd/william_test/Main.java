package com.scau.cfd.william_test;

/**
 * 这个类是：
 * @author: William
 * @date: 2024-11-02T18:01:13CST 18:01
 * @description:
 */

import java.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

class FileSystem {
    private static final int DISK_SIZE = 128;  // 128 blocks
    private static final int BLOCK_SIZE = 64;   // 64 bytes per block
    private int[] fat;                           // File Allocation Table
    private List<DirectoryEntry> rootDirectory; // Root directory
    private int usedBlocks;

    public FileSystem() {
        fat = new int[DISK_SIZE];
        rootDirectory = new ArrayList<>();
        usedBlocks = 0;

        // Initialize FAT
        for (int i = 0; i < DISK_SIZE; i++) {
            fat[i] = 0; // 0 means free
        }
    }

    public static void main(String[] args) {
        FileSystem fs = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        String command;

        while (true) {
            System.out.print("Enter command (create/read/delete/list/exit): ");
            command = scanner.nextLine().toLowerCase();

            switch (command) {
                case "create":
                    System.out.print("Enter file name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter file attribute: ");
                    String attribute = scanner.nextLine();
                    fs.createFile(name, attribute);
                    break;
                case "read":
                    System.out.print("Enter file name: ");
                    name = scanner.nextLine();
                    fs.readFile(name);
                    break;
                case "delete":
                    System.out.print("Enter file name: ");
                    name = scanner.nextLine();
                    fs.deleteFile(name);
                    break;
                case "list":
                    fs.displayFiles();
                    break;
                case "exit":
                    scanner.close();
                    return;
                default:
                    System.out.println("Unknown command.");
            }
        }
    }

    public void createFile(String name, String attribute) {
        // Check for existing file
        for (DirectoryEntry entry : rootDirectory) {
            if (entry.getName().equals(name)) {
                System.out.println("File already exists.");
                return;
            }
        }

        int startBlock = allocateBlock();
        if (startBlock == -1) {
            System.out.println("No space available.");
            return;
        }

        DirectoryEntry newFile = new DirectoryEntry(name, attribute, startBlock, 0);
        rootDirectory.add(newFile);
        System.out.println("File created: " + name);
    }

    public void readFile(String name) {
        for (DirectoryEntry entry : rootDirectory) {
            if (entry.getName().equals(name)) {
                System.out.println("Reading file: " + name);
                // Simulate reading file content
                return;
            }
        }
        System.out.println("File not found.");
    }

    public void deleteFile(String name) {
        DirectoryEntry toRemove = null;
        for (DirectoryEntry entry : rootDirectory) {
            if (entry.getName().equals(name)) {
                toRemove = entry;
                break;
            }
        }
        if (toRemove != null) {
            rootDirectory.remove(toRemove);
            freeBlock(toRemove.getStartBlock());
            System.out.println("File deleted: " + name);
        } else {
            System.out.println("File not found.");
        }
    }

    private int allocateBlock() {
        for (int i = 1; i < DISK_SIZE; i++) { // Start from 1 to skip FAT
            if (fat[i] == 0) {
                fat[i] = -1; // Mark as allocated
                usedBlocks++;
                return i;
            }
        }
        return -1; // No free block found
    }

    private void freeBlock(int blockNumber) {
        fat[blockNumber] = 0; // Mark as free
        usedBlocks--;
    }

    public void displayFiles() {
        if (rootDirectory.isEmpty()) {
            System.out.println("No files in root directory.");
        } else {
            System.out.println("Files in root directory:");
            for (DirectoryEntry entry : rootDirectory) {
                System.out.println("- " + entry.getName());
            }
        }
    }
}

class DirectoryEntry {
    private String name;
    private String attribute;
    private int startBlock;
    private int length;

    public DirectoryEntry(String name, String attribute, int startBlock, int length) {
        this.name = name;
        this.attribute = attribute;
        this.startBlock = startBlock;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public int getStartBlock() {
        return startBlock;
    }

    // Add other getters as needed
}
