package com.github.itstimaifool.markov;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class App 
{
	public static final int BUFFER_LENGTH = 95;
	
    public static void main( String[] args ) throws IOException
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter path to plain text file. ");
        String textFile = scanner.nextLine();
        System.out.println("Enter length of chain. ");
        int chainLength = scanner.nextInt();
        System.out.println("Enter length of text to be generated. ");
        int genLength = scanner.nextInt();
        
        HashMap<String, Probabilities> map = new HashMap<String, Probabilities>();
        FileInputStream fis = new FileInputStream(textFile);
        byte[] buffer = new byte[BUFFER_LENGTH];
        fis.read(buffer, 0, chainLength);
        int length;
        do{
        	length = fis.read(buffer, chainLength + 1, BUFFER_LENGTH - (chainLength + 1));
        	
        	String temp = new String(buffer).toLowerCase();
        	for(int i = 0; i < temp.length() - (chainLength + 1) && i < length; i++){
        		String chain = temp.substring(i, i + chainLength);
        		String next = temp.substring(i + chainLength, i + chainLength + 1);
        		if(map.get(chain) == null){
        			map.put(chain, new Probabilities());
        		}
        		map.get(chain).add(next);
        	}
        	
        	for(int i = 0; i < chainLength + 1; i++){
        		buffer[i] = buffer[BUFFER_LENGTH - (chainLength + 1) + i];
        	}
        }while(length >= 4);
        fis.close();
        
        String cur = new String("It is a truth universally acknowledged, that a single man in possession").substring(0, chainLength).toLowerCase();
        String next;
        for(int i = 0; i < genLength; i++){
        	next = map.get(cur).pickRandom();
        	System.out.print(next);
        	cur += next;
        	//System.out.println(cur);
        	cur = cur.substring(1, chainLength + 1);
        	//System.out.println(cur);
        }
        /*Probabilities prob = new Probabilities();
        prob.add("test");
        prob.add("test");
        prob.add("test");
        prob.add("notest");
        prob.add("notest");
        int test = 0, notest = 0, other = 0;
        for(int i = 0; i < 100000; i++){
        	String blep = prob.pickRandom();
        	if(blep.equals("test")){
        		test++;
        	}else if(blep.equals("notest")){
        		notest++;
        	}else{
        		other++;
        		System.out.println(blep);
        	}
        }
        System.out.println("Test: " + test + " notest: " + notest);*/
    }
    
    static class Probabilities{
    	
    	private LinkedHashMap<String, Integer> counts;
    	private int total;
    	
    	public Probabilities(){
    		counts = new LinkedHashMap<String, Integer>();
    		total = 0;
    	}
    	
    	public void add(String chain){
    		if(counts.get(chain) == null){
    			//System.out.println("Newly adding " + chain);
    			counts.put(chain, 1);
    		}else{
    			//System.out.println("Updating " + chain + " to " + (counts.get(chain) + 1));
    			counts.put(chain, counts.get(chain) + 1);
    		}
    		total++;
    	}
    	
    	public String pickRandom(){
    		int random = (int)(Math.random() * total) + 1;
    		int i = 0;
    		Iterator<String> iterator = counts.keySet().iterator();
    		String next = null;
    		while(iterator.hasNext()){
    			next = iterator.next();
    			i += counts.get(next);
    			if(i >= random){
    				return next;
    			}
    		}
    		return next;
    	}
    }
}
