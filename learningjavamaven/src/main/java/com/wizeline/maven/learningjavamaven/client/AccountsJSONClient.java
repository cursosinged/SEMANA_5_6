/*
 * Copyright (c) 2022 Nextiva, Inc. to Present.
 * All rights reserved.
 */
package com.wizeline.maven.learningjavamaven.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.wizeline.maven.learningjavamaven.model.Post;


@FeignClient(value="getAccountsClient", url="https://jsonplaceholder.typicode.com/")
public interface AccountsJSONClient {
    @GetMapping(value = "/posts/{postId}", produces = "application/json")
    Post getPostById(@PathVariable("postId") Long postId);
}