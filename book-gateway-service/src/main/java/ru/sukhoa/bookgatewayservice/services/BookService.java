package ru.sukhoa.bookgatewayservice.services;

import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class BookService {
    private static final Logger LOGGER = LogManager.getLogger(BookService.class);

    private final RestTemplate restTemplate;

    @Autowired
    public BookService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean reserveBook(String bookName, int count) {
        if (count < 1) {
            throw new IllegalArgumentException("You cannot to order less than one book");
        }

        URI build;
        try {
            build = new URIBuilder("book-service")
                    .setScheme("http")
                    .setPath("books")
                    .addParameter("bookName", bookName)
                    .addParameter("count", Integer.toString(-count)).build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Cannot build the url ", e);
        }
        restTemplate.put(build, null);

        return true;
    }
}
