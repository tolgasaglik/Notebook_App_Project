package com.example.saglik.vocapp2;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "Records")
public class Record extends Model {
    // If name is omitted, then the field name is used.
    @Column(name = "French")
    public String french;

    @Column(name = "English")
    public String english;

    @Column(name = "Initial")
    public String initial;

    public Record() {
        super();
    }

    public Record(String english, String french, String initial) {
        super();
        this.english = english;
        this.french = french;
        this.initial = initial;
    }

    public static Record getRandom() {
        return new Select().from(Record.class).orderBy("RANDOM()").executeSingle();
    }

    static List<Record>getAll(){
        return new Select().from(Record.class).orderBy("English ASC").execute();
    }

    public static List<Record>getAllRandomized(){
        return new Select().from(Record.class).orderBy("RANDOM()").execute();
    }

    public static List<Record>getByInitial(String initial) {
        return new Select().from(Record.class).where("Initial = ?", initial).orderBy("English ASC").execute();
    }

    public static int databaseSize (){
        return getAll().size();
    }

}