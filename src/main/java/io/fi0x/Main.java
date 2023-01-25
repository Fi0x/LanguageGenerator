package io.fi0x;

import io.fi0x.javalogger.logging.LogColor;
import io.fi0x.javalogger.logging.Logger;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main
{
    private static ArrayList<String> vokals = new ArrayList<>();
    private static ArrayList<String> konsonants = new ArrayList<>();

    public static void main(String[] args)
    {
        Logger.getInstance().setDebug(true);
        Logger.getInstance().setVerbose(true);
        Logger.createNewTemplate("Name", LogColor.GREEN, "", false, false, false, true, false, "");

        Logger.log("Programm starting...", Logger.TEMPLATE.INFO);
        loadLanguage();

        Logger.log("How many names?", Logger.TEMPLATE.INFO);
        Scanner sc = new Scanner(System.in);
        int count = 5;
        try
        {
            count = sc.nextInt();
        } catch(InputMismatchException ignored)
        {
        }

        for(; count > 0; count--)
            Logger.log(generateName(), "Name");
    }

    private static void loadLanguage()
    {
        vokals.add("a");
        vokals.add("i");
        vokals.add("o");

        vokals.add("ai");
        vokals.add("an");
        vokals.add("ah");
        vokals.add("aj");
        vokals.add("al");
        vokals.add("ay");
        vokals.add("ia");
        vokals.add("io");
        vokals.add("ih");
        vokals.add("il");
        vokals.add("iv");
        vokals.add("oi");
        vokals.add("og");
        vokals.add("oh");
        vokals.add("ol");
        vokals.add("on");
        vokals.add("ov");
        vokals.add("oy");

        konsonants.add("d");
        konsonants.add("g");
        konsonants.add("h");
        konsonants.add("j");
        konsonants.add("l");
        konsonants.add("n");
        konsonants.add("v");
        konsonants.add("y");

        konsonants.add("da");
        konsonants.add("do");
        konsonants.add("ga");
        konsonants.add("go");
        konsonants.add("gl");
        konsonants.add("gn");
        konsonants.add("gy");
        konsonants.add("ha");
        konsonants.add("hn");
        konsonants.add("hy");
        konsonants.add("ja");
        konsonants.add("jo");
        konsonants.add("la");
        konsonants.add("li");
        konsonants.add("lo");
        konsonants.add("ln");
        konsonants.add("na");
        konsonants.add("no");
        konsonants.add("nh");
        konsonants.add("ny");
        konsonants.add("va");
        konsonants.add("vo");
        konsonants.add("vy");
        konsonants.add("ya");
        konsonants.add("yo");
        konsonants.add("yh");
        konsonants.add("yl");
        konsonants.add("yv");
        konsonants.add("ll");
        konsonants.add("nn");
    }

    private static String generateName()
    {
        StringBuilder name = new StringBuilder();
        for(int i = (int) (Math.random() * 5 + 2); i > 0; i--)
        {
            if(i % 2 == 0)
                name.append(randomVokal());
            else
                name.append(randomKonsonant());
        }
        return name.toString();
    }

    private static String randomVokal()
    {
        return vokals.get((int) (Math.random() * vokals.size()));
    }
    private static String randomKonsonant()
    {
        return konsonants.get((int) (Math.random() * konsonants.size()));
    }
}
