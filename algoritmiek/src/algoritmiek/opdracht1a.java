package algoritmiek;

import java.io.InputStream;
import java.util.*;
import java.util.ArrayList;

class Solution2
{
  public Solution2(){ }   
  // Implement the run method to return the answer to the problem posed by the inputstream.
  
  public static String run(InputStream in) {
    StringBuilder stringb = new StringBuilder();
    List<Person> people = new ArrayList<Person>();
    List<Person> results = new ArrayList<Person>();
    String res = "";
    Person p = new Person("");
    Scanner sc = new Scanner(in);
    int n = sc.nextInt();
    String[] name = new String[n];
    for (int i = 0; i < n; i++){
        sc.next();
        name[i] = sc.next() + " " + sc.next();
    }
    HashMap<String,Integer> peoplecount = new HashMap<String,Integer>(50,10);
    for(String w : name) {
        Integer i = peoplecount.get(w);
        if(i == null) peoplecount.put(w, 1);
        else peoplecount.put(w, i + 1);
    }
    System.out.println(peoplecount.keySet();
    System.out.println(res);
    sc.close();
    return res;
  }
  
}//
class Person implements Comparator<Person>, Comparable<Person>
{
public String name;
public int counter;

  public Person(String nm){
    name = nm;
    counter = 1;
  }
  public String getName(){
    return name;
  }
  
  public boolean equals(Person p){
    return (this.name.equals(p.name));
  }
//Overriding the compareTo method
  public int compareTo(Person d){
		int res = (this.name).compareTo(d.name);
     return res;
  }

  // Overriding the compare method to sort the age 
  public int compare(Person d, Person d1){
     return (d.name).compareTo(d1.name);
  }
}