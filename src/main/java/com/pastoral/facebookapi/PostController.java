package com.pastoral.facebookapi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository repository;

    public PostController(PostRepository repository) {
        this.repository = repository;
    }

    // Create a post
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostRequest request) {
        Post post = new Post(request.getAuthor(), request.getTitle(), request.getContent(), request.getImageUrl());
        Post saved = repository.save(post);
        return ResponseEntity.created(URI.create("/api/posts/" + saved.getId())).body(saved);
    }

    // Get all posts
    @GetMapping
    public List<Post> getAllPosts() {
        return repository.findAll();
    }

    // Get post by id
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update post
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody PostRequest request) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setAuthor(request.getAuthor());
                    existing.setTitle(request.getTitle());
                    existing.setContent(request.getContent());
                    existing.setImageUrl(request.getImageUrl());
                    Post updated = repository.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete post
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        return repository.findById(id)
                .map(existing -> {
                    repository.deleteById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}