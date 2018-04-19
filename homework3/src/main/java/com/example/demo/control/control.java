package com.example.demo.control;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/WordLadder")
public class control {

    public static Map<String,String> userlist = new HashMap<String,String>();
    public static Map<String,String> userinfo = new HashMap<String,String>();
    @RequestMapping("/RestServer")
    public static String main(HttpServletRequest request) throws FileNotFoundException
    {
        String username = request.getParameter("username");
        String start = request.getParameter("start");
        String end = request.getParameter("end");
        if (!userlist.containsKey(username)){
            return "Please login first!";
        }
        HashSet<String> dict;
	    dict = build("D:/src/dictionary.txt");
	    
	    
        control WordLadder = new control();
        String ans = WordLadder.ladder(start,end,dict);
        return ans;
    }

    @RequestMapping("/Login")
    public static String login(HttpServletRequest request){
        readInfo();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (userinfo.get(username).equals(password)){
            userlist.put(username,"1");
            return "You've successfully logged in!";
        }
        else{
            return "Incorrespond username and password!"
        }
    }
    public static void readInfo(){
        userinfo.put("USER","PASSWORD");
        userinfo.put("USER1","PASSWORD1");
        userinfo.put("USER2","PASSWORD2");
    }


    public static HashSet<String> build(String fileName) throws FileNotFoundException
    {
        HashSet<String> dict = new HashSet<String>();
        InputStreamReader reader = new InputStreamReader(new FileInputStream(fileName));
        try {
            BufferedReader br = new BufferedReader(reader);
            String line = null;

            while ((line = br.readLine()) != null) {
                dict.add(line);
            }
            reader.close();
        }
        catch (Exception e) {
            System.out.print("No such dictionary. \n");
        }
        return dict;
    }

    public static String view (String start, String end,Stack<String> stk)
    {
        String ans;
        Iterator<String> it = stk.iterator();
        if( stk.size() == 0)
        {
            ans = "There is not such a word ladder. \n";
            return ans;
        }
        ans = "A word ladder from " + start + " to " + end + " is : " ;
        while(it.hasNext())
        {
            Object obj = it.next();
            ans = ans + obj;
            ans = ans + " ";
        }
        return ans;
    }
    public String ladder(String start, String end, HashSet<String> dict)
    {
        Stack<String> answer = new Stack<String>();
        Queue<Stack<String>> Q = new LinkedList<Stack<String>>();
        HashSet<String> map = dict;
        map.remove(start);
        Stack<String> head = new Stack<String>();
        head.push(start);
        Q.add(head);
        boolean done = false;
        while ((Q.size() != 0) && !done)
        {
            Stack<String> temp = Q.poll();
            HashSet<String> neighbour = new HashSet<String>();
            String temp2 = temp.peek();
            int len = temp2.length();
            for (int i = 0; i < len; i++)
            {
                for (char c ='a';c<= 'z';c++)
                {
                    char[] temp3 = temp2.toCharArray();
                    temp3[i] = c;

                    String newWord = new String(temp3);
                    if (dict.contains(newWord) && (!newWord.equals(temp2)))
                    {
                        neighbour.add(newWord);
                    }
                }
            }
            for (String tempword : neighbour)
            {
                if (map.contains(tempword))
                {
                    if (tempword.equals(end))
                    {
                        temp.push(end);
                        answer = temp;
                        done = true;

                    }
                    else
                    {
                        Stack<String> copy = new Stack<String>();
                        for (String s : temp)
                        {
                            copy.push(s);
                        }
                        copy.push(tempword);
                        Q.add(copy);
                    }
                    map.remove(tempword);
                }
            }
        }
        return view(start,end,answer);
    }
}
