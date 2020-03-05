package com.Projects.AddressProject;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AddressSearchController {

    private ElasticsearchOperations elasticsearchOperations;

    // dependency injection
    public AddressSearchController(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

}
