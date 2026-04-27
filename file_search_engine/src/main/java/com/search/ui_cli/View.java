package com.search.ui_cli;

public class View {
    public void showWelcome() {
        System.out.println("=================================");
        System.out.println("   Local File Search Engine");
        System.out.println("   Type 'exit' to quit");
        System.out.println("=================================");
    }

    public void showIndexingStart() {
        System.out.println("\nIndexing files...");
    }

    public void showExit() {
        System.out.println("Exiting...");
    }

    public void showEmptyQuery() {
        System.out.println("Please enter a valid query.");
    }

    public void showRankingStrategy(String rankingStrategy) {System.out.println("Ranking Strategy: " + rankingStrategy);}

    public void showRankingUpdated() {System.out.println("Ranking updated.");}

}
