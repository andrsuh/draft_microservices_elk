package ru.sukhoa.bookservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sukhoa.bookservice.domain.Book;
import ru.sukhoa.bookservice.domain.BookRepository;

import java.util.List;

@RestController
@RequestMapping("books")
public class BookController {
    @Autowired
    private BookRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    List<Book> getBooks() {
        return repository.findAll();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void addTheBook( @RequestParam String bookName, @RequestParam(defaultValue = "1") int count) {
        Book book = repository.findByName( bookName );
        if (book == null) {
            book = new Book( bookName, count );
        } else {
            if (book.getCount() + count < 0) {
                throw new IllegalStateException( "Not enough books :(" + book.getCount() );
            }
            book.setCount( book.getCount() + count );
        }
        repository.save( book );
    }
}

