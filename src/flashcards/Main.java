package flashcards;

import java.io.*;
import java.util.*;


public class Main
{
    public static void main(String[] args) throws IOException
    {
        String exportFile = "";
        FlashCards flashCards = new FlashCards();
        Scanner scanner = new Scanner(System.in);
        boolean exitFlag = false;
        // interpret command line args
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-import":
                    flashCards.importCard(args[i + 1]);
                    break;
                case "-export":
                    exportFile = args[i + 1];
                    break;
                default:
                    // do nothing
            }
        }
        while (!exitFlag)
        {
            System.out.println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            flashCards.addLog("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):\n");
            scanner = new Scanner(System.in);
            String choose=scanner.nextLine();
            flashCards.addLog(choose+"\n");

            switch (choose) {
                case "add":
                    flashCards.addCard(flashCards.map);
                    break;
                case "remove":
                    flashCards.removeCard(flashCards.map, flashCards.hardMap);
                    break;
                case "import":
                    flashCards.importCardsFromFileFromConsole();
                    break;
                case "export":
                    flashCards.exportCardsToFileFromConsole();
                    break;
                case "ask":
                    flashCards.askCard(flashCards.map);
                    break;
                case "log":
                    flashCards.getLogs(flashCards.log);
                    break;
                case "hardest card":
                    flashCards.getHardestCard(flashCards.hardMap);
                    break;
                case "reset stats":
                    flashCards.resetStats(flashCards.hardMap);
                    break;
                case "exit":
                    System.out.println("Bye bye!");
                    if (!"".equals(exportFile)) {
                        flashCards.exportCard(exportFile);
                    }
                    exitFlag = true;
                    break;
                default:
                    System.out.println("Invalid option. Try again...");
                    flashCards.addLog("Invalid option. Try again...");

            }
        }
    }
}

