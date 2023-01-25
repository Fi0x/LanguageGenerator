package io.fi0x;

import io.fi0x.javalogger.logging.Logger;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class Main
{
    private static final ArrayList<String> vokals = new ArrayList<>();
    private static final ArrayList<String> konsonants = new ArrayList<>();
    private static final ArrayList<String> vokalKonsonant = new ArrayList<>();
    private static final ArrayList<String> konsonantVokals = new ArrayList<>();

    public static void main(String[] args)
    {
        Setup.initializeLogger();

        Logger.log("Programm starting...", LOG.INFO);
        loadLanguage();

        Logger.log("How many names would you like to generate?", LOG.QUESTION);
        Scanner sc = new Scanner(System.in);
        int count = 5;
        try
        {
            count = sc.nextInt();
        } catch(InputMismatchException ignored)
        {
        }

        for(; count > 0; count--)
            Logger.log(generateName(), LOG.OUTPUT);
    }

    private static void loadLanguage()
    {
        vokals.add("a");
        vokals.add("i");
        vokals.add("o");
        vokals.add("ai");
        vokals.add("ia");
        vokals.add("io");
        vokals.add("oi");

        vokalKonsonant.add("an");
        vokalKonsonant.add("ah");
        vokalKonsonant.add("aj");
        vokalKonsonant.add("al");
        vokalKonsonant.add("ay");
        vokalKonsonant.add("ih");
        vokalKonsonant.add("il");
        vokalKonsonant.add("iv");
        vokalKonsonant.add("og");
        vokalKonsonant.add("oh");
        vokalKonsonant.add("ol");
        vokalKonsonant.add("on");
        vokalKonsonant.add("ov");
        vokalKonsonant.add("oy");

        konsonants.add("d");
        konsonants.add("g");
        konsonants.add("h");
        konsonants.add("j");
        konsonants.add("l");
        konsonants.add("n");
        konsonants.add("v");
        konsonants.add("y");
        konsonants.add("gl");
        konsonants.add("gn");
        konsonants.add("gy");
        konsonants.add("hn");
        konsonants.add("hy");
        konsonants.add("ln");
        konsonants.add("nh");
        konsonants.add("ny");
        konsonants.add("vy");
        konsonants.add("yh");
        konsonants.add("yl");
        konsonants.add("yv");
        konsonants.add("ll");
        konsonants.add("nn");

        konsonantVokals.add("da");
        konsonantVokals.add("do");
        konsonantVokals.add("ga");
        konsonantVokals.add("go");
        konsonantVokals.add("ha");
        konsonantVokals.add("ja");
        konsonantVokals.add("jo");
        konsonantVokals.add("la");
        konsonantVokals.add("li");
        konsonantVokals.add("lo");
        konsonantVokals.add("na");
        konsonantVokals.add("no");
        konsonantVokals.add("va");
        konsonantVokals.add("vo");
        konsonantVokals.add("ya");
        konsonantVokals.add("yo");
    }

    private static String generateName()
    {
        StringBuilder name = new StringBuilder();
        for(int i = 0; i < (int) (Math.random() * 5 + 2); i++)
        {
            switch(i % 4)
            {
                case 0 -> name.append(vokalEnd());
                case 1 -> name.append(randomKonsonant());
                case 2 -> name.append(vokalStart());
                default -> name.append(randomVokal());
            }
        }

        name.setCharAt(0, String.valueOf(name.charAt(0)).toUpperCase(Locale.ROOT).charAt(0));
        return name.toString();
    }

    private static String vokalEnd()
    {
        return konsonantVokals.get((int) (Math.random() * konsonantVokals.size()));
    }
    private static String vokalStart()
    {
        return vokalKonsonant.get((int) (Math.random() * vokalKonsonant.size()));
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
