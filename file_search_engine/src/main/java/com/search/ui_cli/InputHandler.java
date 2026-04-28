package com.search.ui_cli;

import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner = new Scanner(System.in);

    public String readQuery() {
        System.out.print("Search > ");
        return scanner.nextLine();
    }

    public String readRankingStrategy() {
        System.out.print("New Ranking Strategy : ");
        return scanner.nextLine();
    }

    public String readPrefix() {
        return scanner.nextLine();
    }
}
