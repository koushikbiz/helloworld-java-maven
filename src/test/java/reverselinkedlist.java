import java.util.*;

public class reverselinkedlist {

public static void main (String args[]) {

LinkedList<String> ll = new LinkedList<String>();
LinkedList<String> lr = new LinkedList<String>();
ll.add("Test");

System.out.println("");
Iterator<String> itr = ll.iterator();
while (itr.hasNext()) {
System.out.println(itr.next());
}

System.out.println("");
lr = reverselist(ll);
}

public static LinkedList<String> reverselist (LinkedList<String> abc) {
LinkedList<String> lr = new LinkedList<String>();
Iterator<String> itr = abc.iterator();
while (itr.hasNext()) {
lr.addFirst(itr.next());
}
return lr;
}
}