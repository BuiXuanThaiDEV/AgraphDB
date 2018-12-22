import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.lang.Object;
import java.util.Date;
import java.util.Random;
import java.util.logging.SimpleFormatter;

import org.eclipse.rdf4j.query.algebra.Str;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.apache.jena.atlas.json.JSON;
import org.json.simple.parser.ParseException;
import org.mapdb.Atomic;

public class GenPaper {
    static int CURRENT_PAPER_ID = 0;

    public static Paper genTypeA() {
        int paper_id = ++CURRENT_PAPER_ID;
//        String link = "http://ex/";
//        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        Date date1 = new Date();
//
//        Source source = new Source(link+ paper_id,format.format(date1));
        Source source = genSource(paper_id);
        Person person1 = GenPaper.genPerson(paper_id, source);
        Event event1 = GenPaper.genEvent(paper_id, source);
        Location location1 = GenPaper.genLocation(paper_id, source);
        Relationship rel1 = new Relationship(person1, event1, "attend");
        Relationship rel2 = new Relationship(event1, location1, "at");
        Relationship[] relationships = {rel1, rel2};
        ParentObject[] parentObjects = {person1, event1, location1};
        Paper paper = new Paper(parentObjects, relationships);
        return paper;

    }

    public static Source genSource(int paper_id) {
        JSONParser parser = new JSONParser();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        FileReader fileReader = null;
        try {
            fileReader = new FileReader("data/LINK.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Object object = null;
        try {
            object = parser.parse(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = (JSONArray) object;
        Random random = new Random();
        JSONObject jsonObject = (JSONObject) jsonArray.get(random.nextInt(1000));
        String link = (String) jsonObject.get("link") + paper_id;


        return new Source(link, format.format(date));
    }
    /// thiết kế lại  để chỉ đọc file 1 lần trả về 1 mảng JSONOBject. sau đó mỗi lần gen thì lấy random trong mảng đó.
    /// chứ nếu mỗi lần gen 1 bài báo lại đọc mấy fiile json 1 lần thì vài lần là tràn .
    /// sửa lại phần lặp code nữa

    public static Person genPerson(int paper_id, Source source) {
        Person person = null;
        JSONParser parser = new JSONParser();
        try {
            FileReader fileReader = new FileReader("data/PERSON.json");
            Object object = new Object();
            object = parser.parse(fileReader);
            JSONArray jsonArray = (JSONArray) object;
            Random random = new Random();

            JSONObject jsonObject = (JSONObject) jsonArray.get(random.nextInt(1000));
            String label = (String) jsonObject.get("label") + "|" + paper_id;
            String description = (String) jsonObject.get("description");
            Long age = (Long) jsonObject.get("age");
            Integer ageInt = age.intValue();

            String nationality = (String) jsonObject.get("nationality");
            person = new Person(paper_id, label, description, source, "person", ageInt, nationality);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return person;
    }

    public static Event genEvent(int paper_id, Source source) {
        Event event = null;
        JSONParser parser = new JSONParser();
        try {
            FileReader fileReader = new FileReader("data/EVENT.json");
            Object object = parser.parse(fileReader);
            JSONArray jsonArray = (JSONArray) object;
            Random random = new Random();

            JSONObject jsonObject = (JSONObject) jsonArray.get(random.nextInt(1000));
            String label1 = (String) jsonObject.get("label1");
            String label2 = String.valueOf(jsonObject.get("label2")) + "|" + paper_id;
            String label = "Launches new product: " + label1 + label2;
            String description = (String) jsonObject.get("description");
            event = new Event(paper_id, label, description, source, "event");


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return event;
    }

    public static Location genLocation(int paper_id, Source source) {
        Location location = null;
        JSONParser parser = new JSONParser();
        try {
            FileReader fileReader = new FileReader("data/LOCATION.json");
            Object object = parser.parse(fileReader);
            JSONArray jsonArray = (JSONArray) object;
            Random random = new Random();

            JSONObject jsonObject = (JSONObject) jsonArray.get(random.nextInt(1000));
            String label = jsonObject.get("label") + "|" + paper_id;
            String country = (String) jsonObject.get("country");
            String description = (String) jsonObject.get("description");
            location = new Location(paper_id, label, description, source, "location", country);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return location;
    }

    public static void main(String[] args) {

//        Source source = genSource(CURRENT_PAPER_ID);
//        Person person = genPerson(CURRENT_PAPER_ID,source);
//        Event event = genEvent(CURRENT_PAPER_ID,source);
//        Location location = genLocation(CURRENT_PAPER_ID,source);
//        person.displayInfor();
//        event.displayInfor();
//        location.displayInfor();
        Paper paper = genTypeA();
        paper.showInfor();

    }

}
