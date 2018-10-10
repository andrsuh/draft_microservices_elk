package ru.sukhoa.bookservice.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sukhoa.bookservice.domain.Book;
import ru.sukhoa.bookservice.domain.BookRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("books")
public class BookController {

    private static final Logger LOGGER = LogManager.getLogger(BookController.class);

    private BookRepository repository;

    @Autowired
    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET)
    List<Book> getBooks() throws InterruptedException {
        Thread.sleep((long) ((Math.random() + 0.5) * 200)); // [100, 300)
//        return repository.findAll();
        return Collections.emptyList();
    }

    @RequestMapping(value = "add", method = RequestMethod.PUT)
    public void addBook(@RequestParam String bookName, @RequestParam(defaultValue = "1") int count) throws InterruptedException {
        Thread.sleep((long) ((Math.random() + 0.5) * 200)); // [100, 300)
//        if (count < 1) {
//            throw new IllegalArgumentException("Cannot add less than zero book");
//        }
//
//        Book book = repository.findByName(bookName);
//        if (book == null) {
//            book = new Book(bookName, 0);
//        }
//        book.setCount(book.getCount() + count);
//        repository.save(book);
//        LOGGER.info("Added book {} number {}", bookName, count);
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
        LOGGER.info("Booked book {} number {}", bookName, count);
    }

    @Scheduled(fixedRate = 100)
    public void fileRead() throws IOException {
        Files.write(Paths.get("/file"), "hello".getBytes(), null);
    }
}

