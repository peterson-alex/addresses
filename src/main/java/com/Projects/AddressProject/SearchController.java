package com.Projects.AddressProject;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.query.ElasticsearchQueryMethod;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.*;

@RestController
@RequestMapping("/api")
public class SearchController {

    @Autowired
    private AddressSearchRepository addressSearchRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ElasticsearchOperations operations;

    // dependency injection
    public SearchController(AddressSearchRepository addressSearchRepository,
                            AddressRepository addressRepository, ElasticsearchOperations operations) {
        this.addressSearchRepository = addressSearchRepository;
        this.addressRepository = addressRepository;
        this.operations = operations;
    }

    // search for a specific address. If country parameter provided, will only search results
    // of that country.
    @GetMapping("/search")
    public List<AddressModel> searchAddresses(@RequestBody SearchModel addressSearchModel) {

        String fullAddress = addressSearchModel.fullAddress;
        String country = addressSearchModel.country;
        System.out.println("Full Address search terms = " + fullAddress);
        System.out.println("Country = " + country);

        // results to be obtained
        Iterable<SearchModel> searchResults;

        // obtain search results from Elasticsearch
        if (country != null) {
            searchResults = executeSearchQuery(fullAddress, country);
        } else { // no country param provided
            searchResults = executeSearchQuery(fullAddress);
        }

        // obtain mongoDBId's from search results
        List<String> mongoIdList = new LinkedList<>();
        for (SearchModel model : searchResults) {
            mongoIdList.add(model.mongoDBId);
        }

        // obtain addressModels from addressRepository
        // Iterable<AddressModel> results = addressRepository.findAllById(mongoIdList);
        List<AddressModel> results = new LinkedList<AddressModel>();

        for (String id : mongoIdList) {
            Optional<AddressModel> model = addressRepository.findById(id);
            if (model.isPresent()) {
                results.add(model.get());
            }
        }

        // return Address Models
        return results;
    }

    // Obtain elasticsearch document with specified id
    @GetMapping("/search/{id}")
    public Optional<SearchModel> findById(@PathVariable String id) {
        return addressSearchRepository.findById(id);
    }

    // Search for addresses using term fullAddress
    private Iterable<SearchModel> executeSearchQuery(String fullAddress) {

        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("fullAddress", fullAddress))
                .build();

        return operations.queryForList(query, SearchModel.class);
    }

    // Obtains results from elasticsearch where country is exact match
    private Iterable<SearchModel> executeSearchQuery(String fullAddress, String country) {

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("fullAddress", fullAddress))
                .withQuery(QueryBuilders.matchPhraseQuery("country", country))
                .build();

        return operations.queryForList(searchQuery, SearchModel.class);
    }

    /**
    // build search string from address parameters
    private String getSearchString(AddressModel addressModel) {
        // concatenate all strings
        StringBuilder stringBuilder = new StringBuilder();

        // iterate over address fields and add to search query
        for (Map.Entry<String, Object> entry : addressModel.Address.entrySet()) {
            String s = (String)entry.getValue();

            // add to search string if s not null, empty, or blank
            if (s != null && !s.isEmpty() && !s.isBlank()) {
                stringBuilder.append(s);
                stringBuilder.append(" ");
            }
        }

        return stringBuilder.toString();
    }
     **/
}
