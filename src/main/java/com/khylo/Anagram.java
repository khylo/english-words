package com.khylo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.java.util.stream.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Collectors;
import java.util.Collection;

public class Anagram{
    Collection<String> realWordSet;
    /* code for streaming characters.. Not code-points important
    String s = "abc-de3-2fg";
String s1 = s.chars().filter(Character::isLetter)
            .collect(StringBuilder::new,StringBuilder::appendCodePoint,StringBuilder::append)
            .toString();
System.out.println(s1);
*/

    Anagram() throws IOException{
        loadRealWords();
    }

    private void loadRealWords() throws IOException{
       String dir =  System.getProperty("user.dir");
       Path path = Paths.get(dir+"/words_alpha.txt");
       realWordSet = new HashSet<>();
       try(Stream<String> lines = Files.lines(path)){
            lines.forEach(s -> realWordSet.add(s));
       }
       log(realWordSet.size()+" real words loaded");
    }

    public static void log(Object s){
        System.out.println(s);
    }

    public Set<String> getAnagrams(String s, int subAnagramsMin){
        return getAnagrams(s, subAnagramsMin, true);
    }
    public Set<String> getAnagrams(String s, int subAnagramsMin,boolean realWordsOnly){
        Set<String> ret = new HashSet<>();
        //Set<String> charSet = new HashSet<>();charSet.add("a");charSet.add("b");charSet.add("c");
        //Set<String> charSet = Set.of(s.split(".")); // java 9+ see https://stackoverflow.com/questions/3064423/how-to-convert-an-array-to-a-set-in-java
        Collection<String> charSet = new ArrayList<>(Arrays.asList(s.split("")));
        //Set<String> charSet = s.chars().map(cp ->new StringBuilder().appendCodePoint(cp).toString()).collect( Collectors.toCollection(HashSet::new));
        log("Total anagrams = "+numAnagrams(s));
        anagram("", charSet, ret,subAnagramsMin, realWordsOnly);
        return ret;
    }

    private void anagram(String sanagram, Collection<String> stringSet, Set<String> ret, int subAnagramsMin, boolean realWordsOnly){
        if(stringSet.size()==0){
            if(realWordsOnly){
                if(!realWordSet.contains(sanagram))
                    return;
            }
            ret.add( sanagram);
        }
        if(sanagram.length()>=subAnagramsMin){
            if(realWordSet.contains(sanagram))
                ret.add(sanagram);
        }
        //stringSet.stream().forEach(s -> anagram(sanagram+s,
        stringSet.stream().forEach(s -> {
            List subset = new ArrayList(stringSet);
            subset.remove(s); 
            anagram(sanagram+s, subset, ret, subAnagramsMin, realWordsOnly);  
        });
            
        // originally had this, but if there were repeating letters it would remove them both, added  findFirst but that wont work. Need some global counter
        //         stringSet.stream().filter(
        //             i -> !i.equals(s)).findFirst()
        //         .collect(Collectors.toSet()), ret));
         return ;
    }

    public static int numAnagrams(String s){
        Set<String> set = new HashSet<>(Arrays.asList(s.split("")));
        return factorial(s.length());
    }

    public static int factorial(int i){
        if(i==1)
            return 1;
        return i*factorial(i-1);
    }

    

    public static void main(String args[])throws IOException{
        //Set<String> set = getAnagrams("soartrip");
        Anagram anagram = new Anagram();
        //Set<String> set = anagram.getAnagrams("abb", true);
        //Set<String> set = anagram.getAnagrams("soartrip", 4, true);
        Set<String> set = anagram.getAnagrams("moneyl", 4, true);    
        Anagram.log(set.size()+" Results");
        Anagram.log(set);
        //Anagram.log(getAnagrams("soartrip"));
    }
}