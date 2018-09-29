package ru.sukhoa.bookservice.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Book {

    @Id
    private long id = ( int ) ( Math.random() * 100_000 );
    private String name;
    private int count;

    public Book() {
    }

    public Book( String name ) {
        this( name, 10 );
    }

    public Book( String name, int count ) {
        this.name = name;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount( int count ) {
        this.count = count;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
