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

    private BookRepository repository;

    @Autowired
    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET)
    List<Book> getBooks() {
        return repository.findAll();
    }

    @RequestMapping(value = "add", method = RequestMethod.PUT)
    public void addBook(@RequestParam String bookName, @RequestParam(defaultValue = "1") int count) {
        if (count < 1) {
            throw new IllegalArgumentException("Cannot add less than zero book");
        }

        Book book = repository.findByName(bookName);
        if (book == null) {
            book = new Book(bookName, 0);
        }
        book.setCount(book.getCount() + count);
        repository.save(book);
    }

    @RequestMapping(value = "withdraw", method = RequestMethod.PUT)
    public void withdrawBook(@RequestParam String bookName, @RequestParam(defaultValue = "1") int count) {
        if (count < 1) {
            throw new IllegalArgumentException("Cannot withdraw less than one book");
        }
        Book book = repository.findByName(bookName);
        if (book == null) {
            throw new IllegalArgumentException("Such book doesn't exists : " + bookName);
        }
        if (book.getCount() - count < 0) {
            throw new IllegalArgumentException("Not enough book to withdraw  : " + bookName +
                    " wanted " + count + ", " + book.getCount() + " left");
        }
        book.setCount(book.getCount() - count);
        repository.save(book);
    }
}

