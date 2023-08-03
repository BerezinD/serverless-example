package dev.example.controller;

import dev.example.JsonPlaceholderService;
import dev.example.exceptions.PostNotFoundException;
import dev.example.exceptions.PostValidationException;
import dev.example.post.Post;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostController.class);
    private final JsonPlaceholderService jsonPlaceholderService;
    /**
     * Only for educational purposes, to store data.
     */
    private List<Post> posts = new ArrayList<>();

    public PostController(JsonPlaceholderService jsonPlaceholderService) {
        this.jsonPlaceholderService = jsonPlaceholderService;
    }

    @GetMapping
    List<Post> findAll() {
        return posts;
    }

    @GetMapping("/{id}")
    Optional<Post> findById(@PathVariable Integer id) {
        return Optional.ofNullable(posts.stream()
                .filter(p -> p.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new PostNotFoundException("Post with id: {} not found.", id)));
    }

    @PostMapping
    void create(@RequestBody Post post) {
        validatePostIdNonNull(post);
        posts.add(post);
    }

    @PutMapping("/{id}")
    void update(@RequestBody Post post, @PathVariable Integer id) {
        validatePostIdNonNull(post);
        if (!Objects.equals(post.id(), id)) {
            throw new PostValidationException("Provided post IDs in the payload and request parameters do not match.");
        }
        posts.stream()
                .filter(p -> p.id().equals(id))
                .findFirst()
                .ifPresent(p -> posts.set(posts.indexOf(p), post));
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id) {
        posts.removeIf(p -> p.id().equals(id));
    }

    void validatePostIdNonNull(Post post) {
        if (post.id() == null)
            throw new PostValidationException("Post id could not be null.");
    }

    /**
     * Initialize with some fake data.
     */
    @PostConstruct
    private void init() {
        if (posts.isEmpty()) {
            LOGGER.info("Loading initial posts using JsonPlaceholderService");
            posts = jsonPlaceholderService.loadPosts();
        }
    }
}
