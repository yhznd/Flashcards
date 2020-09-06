package flashcards;

import java.io.*;
import java.util.*;

public class FlashCards
{
    public static Map<String, String> map = new HashMap<String, String>();
    public static Map<String, Integer>  hardMap = new LinkedHashMap<>();
    public static ArrayList<String> log =new ArrayList<>();

    public static void addCard(Map<String, String> map) //Adding Card
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("The card:");
        log.add("The card:\n");
        String card = sc.nextLine();
        log.add(card+"\n");
        String definition = "";

        if (map.containsKey(card)) {
            System.out.println("The card " + '"' + card + '"' + " already exists.");
            log.add("The card " + '"' + card + '"' + " already exists.\n");
            return;
        } else {
            System.out.println("The definition:");
            log.add("The definition:\n");
            definition = sc.nextLine();
            log.add(definition+"\n");
        }

        if (map.containsValue(definition)) {
            System.out.println("The definition " + '"' + definition + '"' + " already exists.");
            log.add("The definition " + '"' + definition + '"' + " already exists.\n");
        } else
        {
            map.put(card, definition);
            System.out.println("The pair (" + '"' + card + '"' + ":" + '"' + definition + '"' + ") has been added");
            log.add("The pair (" + '"' + card + '"' + ":" + '"' + definition + '"' + ") has been added\n");
        }

    }

    public static void removeCard(Map<String, String> map,Map<String,Integer> hardMap) {
        Scanner sc = new Scanner(System.in);
        System.out.println("The card:");
        log.add("The card:\n");
        String card = sc.nextLine();
        log.add(card+"\n");

        if (map.containsKey(card))
        {
            map.remove(card);
            if(hardMap.containsKey(card))
            {
                hardMap.remove(card);
            }
            System.out.println("The card has been removed.");
            log.add("The card has been removed.\n");
        }
        else {
            System.out.println("Can't remove " + '"' + card + '"' + ": there is no such card.");
            log.add("Can't remove " + '"' + card + '"' + ": there is no such card.\n");
        }

    }

    public static void askCard(Map<String, String> map)
    {
        System.out.println("How many times to ask?");
        log.add("How many times to ask?\n");
        Map<String, String> treeMap = new TreeMap<String, String>(map);
        Scanner sc = new Scanner(System.in);
        int times = Integer.parseInt(sc.nextLine());
        log.add(times+"\n");
        if (!map.isEmpty()) {
            for (int a = 0; a < times; a++) {
                String randomName = (String) map.keySet().toArray()[new Random().nextInt(map.keySet().toArray().length)]; //random key

                System.out.println("Print the definition of " + '"' + randomName + '"');
                log.add("Print the definition of " + '"' + randomName + '"'+"\n");
                String defx = sc.nextLine();
                log.add(defx+"\n");

                if (defx.equals(map.get(randomName))) {
                    System.out.println("Correct answer.");
                    log.add("Correct answer.\n");
                } else if (defx.equals("??")) {
                    System.out.println("Wrong answer. The right answer is " + '"' + map.get(randomName) + '"' + ".");
                    log.add("Wrong answer. The right answer is " + '"' + map.get(randomName) + '"' + ".\n");
                    addHardestCard(hardMap,randomName);
                } else {
                    if (getRelatedKey(map, defx).equals("")) {
                        System.out.println("Wrong answer. The right answer is " + '"' + map.get(randomName) + '"' + ".");
                        log.add("Wrong answer. The right answer is " + '"' + map.get(randomName) + '"' + ".\n");
                        addHardestCard(hardMap,randomName);
                    }
                    else
                    {
                        System.out.println("Wrong answer. The right answer is " + '"' + map.get(randomName) + '"'
                                + ", but your definition is correct for " + '"' + getRelatedKey(map, defx) + '"' + ".");
                        log.add("Wrong answer. The right answer is " + '"' + map.get(randomName) + '"'
                                + ", but your definition is correct for " + '"' + getRelatedKey(map, defx) + '"' + ".\n");
                        addHardestCard(hardMap,randomName);
                    }
                }

            }
        }

    }

    public static void exportCard(String fileName) throws IOException
    {
        File file = new File(fileName);
        int counter = 0;

        try (FileWriter fw = new FileWriter(file))
        {
            for (Map.Entry<String, String> pair : map.entrySet())
            {
                String mistakes = String.valueOf(getMistakeCount(hardMap,pair.getKey()));
                fw.write(pair.getKey());
                fw.append("\n");
                fw.write(pair.getValue());
                fw.append("\n");
                fw.write(mistakes);
                fw.append("\n");
                counter++;
            }
            fw.flush();
        }
        log.add(counter + " cards have been saved.");
        System.out.println(counter + " cards have been saved.");
    }

    public static void importCard(String fileName) {

        String card,definition;
        int mistakeCount;
        int lines=0;
        File file = new File(fileName);
        try (Scanner inFile = new Scanner(file))
        {
            while (inFile.hasNextLine())
            {
                card = inFile.nextLine();
                definition = inFile.nextLine();
                mistakeCount = Integer.parseInt(inFile.nextLine());
                if (!"null".equals(definition))
                {
                    map.put(card, definition);
                    hardMap.put(card, mistakeCount);
                    lines++;
                }
            }

            System.out.println(lines + " cards have been loaded.");
            log.add(lines + " cards have been loaded.\n");
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("The file " + fileName + " not found.");
            log.add("The file " + fileName + " not found.\n");
        }
    }

    public static String getRelatedKey(Map<String, String> comingMap, String comingDef) {
        String result = "";

        for (String i : comingMap.keySet()) {
            if (comingMap.get(i).equals(comingDef)) {
                result = i;
            }
        }

        return result;
    }

    public static void getLogs(ArrayList<String> list) throws IOException
    {
        System.out.println("File name:");
        log.add("File name:\n");
        Scanner sc=new Scanner(System.in);
        String fileName=sc.nextLine();
        log.add(fileName+"\n");
        File changedFile=new File(fileName);
        Writer writer =new FileWriter(changedFile);

        for (String i :list)
        {
            writer.write(i);
        }
        writer.close();
        System.out.println("The log has been saved.");
        log.add("The log has been saved.\n");

    }

    public static void addHardestCard(Map<String, Integer> comingMap,String card) {
        if(!comingMap.containsKey(card))
        {
            comingMap.put(card,1);
        }
        else
        {
            int count=comingMap.get(card);
            comingMap.put(card,count+1);
        }

    }

    public static void getHardestCard(Map<String, Integer> comingMap)
    {
        ArrayList<String> hardestCards = new ArrayList<>();
        int max = 1;
        for (String  i : comingMap.keySet())
        {
            Integer v = comingMap.get(i);
            if (v > max)
            {
                max = v;
                hardestCards.clear();
                hardestCards.add(i);
            } else if (v == max) {
                hardestCards.add(i);
            }
        }
        switch (hardestCards.size()) {
            case 0:
                String s = "There are no cards with errors.";
                System.out.println(s);
                log.add(s);
                break;
            case 1:
                s = "The hardest card is \"" + hardestCards.get(0) + "\". You have " + max + " errors answering it.";
                System.out.println(s);
                log.add(s);
                break;
            default:
                String cards ="";
                for (int i = 0; i < hardestCards.size(); i++) {
                    if (i < hardestCards.size() - 1) {
                        cards += "\"" + hardestCards.get(i) + "\", ";
                    } else {
                        cards += "\"" + hardestCards.get(i) + "\". ";
                    }
                }
                s = "The hardest cards are " + cards + "You have " + max + " errors answering them.";
                System.out.println(s);
                log.add(s);
        }
    }
    public static void resetStats(Map<String, Integer> comingMap)
    {
        for (String i : comingMap.keySet())
        {
            comingMap.put(i,0);
        }
        System.out.println("Card statistics has been reset.");
        log.add("Card statistics has been reset.\n");
    }

    public static void importCardsFromFileFromConsole() throws FileNotFoundException {
        System.out.println("File name:");
        log.add("File name:\n");
        Scanner sc = new Scanner(System.in);
        String fileName = sc.nextLine();
        log.add(fileName+"\n");
        importCard(fileName);
    }

    public static void exportCardsToFileFromConsole() throws IOException {
        System.out.println("File name:");
        log.add("File name:\n");
        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        log.add(fileName+"\n");
        exportCard(fileName);
    }

    public static void addLog(String s)
    {
        log.add(s);
    }

    public static int getMistakeCount(Map<String,Integer> map, String card)
    {
        try
        {
            return map.get(card);

        }
        catch (NullPointerException ex)
        {
            return 0;
        }
    }
}
