package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class Main {

    public static Map<String, String> map;
    public static void main(String[] args) throws IOException {
        map=new HashMap<String, String>();
        String choose="";
        do {
            choose= callMenu();
            switch (choose)
            {
                case "add":
                    addCard(map);
                    break;
                case "remove":
                    removeCard(map);
                    break;
                case "import":
                    importCard(map);
                    break;
                case "export":
                    exportCard(map);
                    break;
                case "ask":
                    askCard(map);
                    break;
            }
        }
        while (!choose.equals("exit"));
        System.out.println("Bye bye!");
    }

    public static String callMenu()
    {
        System.out.println("Input the action (add, remove, import, export, ask, exit):");
        Scanner sc=new Scanner(System.in);
        return sc.nextLine();

    }
    public static void addCard(Map<String,String>  map) //Adding Card
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("The card:");
        String card = sc.nextLine();
        String definition ="";

        if (map.containsKey(card))
        {
            System.out.println("The card " + '"' + card + '"' + " already exists.");
            return;
        }

        else
        {
            System.out.println("The definition:");
            definition = sc.nextLine();
        }

        if (map.containsValue(definition)) {
            System.out.println("The definition " + '"' + definition + '"' + " already exists.");
        }

        else
        {
            map.put(card, definition);
            System.out.println("The pair (" + '"' + card + '"' + ":" + '"' + definition + '"' + ") has been added");
        }

    }

    public static void removeCard(Map<String,String>  map)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("The card:");
        String card = sc.nextLine();

        if(map.containsKey(card))
        {
            map.remove(card);
            System.out.println("The card has been removed.");
        }
        else
            System.out.println("Can't remove "+'"'+card+'"'+": there is no such card.");

    }
    public static void askCard(Map<String,String>  map)
    {
        System.out.println("How many times to ask?");
        Map<String, String> treeMap = new TreeMap<String, String>(map);
        Scanner sc = new Scanner(System.in);
        int times=Integer.parseInt(sc.nextLine());

        if(!map.isEmpty())
        {
            for (int a = 0; a < times; a++) {
                String randomName = (String) map.keySet().toArray()[new Random().nextInt(map.keySet().toArray().length)]; //random key

                System.out.println("Print the definition of " + '"' + randomName + '"');
                String defx = sc.nextLine();

                if (defx.equals(map.get(randomName))) {
                    System.out.println("Correct answer.");
                } else if (defx.equals("??")) {
                    System.out.println("Wrong answer. The correct one is " + '"' + map.get(randomName) + '"' + ".");
                } else {
                    if (getRelatedKey(map, defx).equals(""))
                        System.out.println("Wrong answer. The correct one is " + '"' + map.get(randomName) + '"' + ".");
                    else
                        System.out.println("Wrong answer. The correct one is " + '"' + map.get(randomName) + '"'
                                + ", you've just written the definition of " + '"' + getRelatedKey(map, defx) + '"' + ".");
                }

            }
        }

    }

    public static void exportCard(Map<String,String>  map) throws IOException {
        System.out.println("File name:");
        Scanner sc = new Scanner(System.in);
        String fileName=sc.nextLine();

        File file=new File(fileName);
        FileWriter writer=new FileWriter(file); //overrides the file, default false
        for (String i:map.keySet())
        {
            writer.write(i);
            writer.write("\n");
            writer.write(map.get(i));
            writer.write("\n");

        }
        writer.close();
        System.out.println(map.size() + " cards have been saved.");

    }
    public static void importCard(Map<String,String>  map) throws FileNotFoundException {
        System.out.println("File name:");
        Scanner sc = new Scanner(System.in);
        String fileName=sc.nextLine();
        try
        {
            File file=new File(fileName);
            Scanner scRead = new Scanner(file);
            int lines = 0;
            while (scRead.hasNext())
            {
                String card = scRead.nextLine();
                String definition = scRead.nextLine();

                if (map.containsValue(definition))
                {
                    Iterator<String> iterator = map.keySet().iterator();
                    while(iterator.hasNext())
                    {
                        String cardX = iterator.next();
                        if(cardX.equals(card))
                        {
                            iterator.remove();
                        }
                    }
                }
                map.put(card, definition);
                lines++;
            }
            System.out.println(lines + " cards have been loaded.");
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("The file " + fileName + " not found.");
        }

    }


    public static String getRelatedKey(Map<String,String> comingMap ,String comingDef)
    {
        String result="";

        for(String i: comingMap.keySet())
        {
            if(comingMap.get(i).equals(comingDef))
            {
                result=i;
            }
        }

        return result;
    }
}
