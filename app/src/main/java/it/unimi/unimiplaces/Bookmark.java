package it.unimi.unimiplaces;

/**
 * convenience Bookmark object class
 */
public class Bookmark {
    public long id;
    public String type;
    public String identifier;
    public String title;


    public Bookmark(String type,String identifier,String title){
        this.type       = type;
        this.identifier = identifier;
        this.title      = title;
    }

    public Bookmark(long id, String type,String identifier,String title){
        this.id         = id;
        this.type       = type;
        this.identifier = identifier;
        this.title      = title;
    }
}
