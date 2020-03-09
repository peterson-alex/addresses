package com.Projects.AddressProject;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.web.bind.annotation.*;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SearchController {

    @Autowired
    private AddressSearchRepository addressSearchRepository;

    @Autowired
    private AddressRepository addressRepository;

    // dependency injection
    public SearchController(AddressSearchRepository addressSearchRepository,
                                   AddressRepository addressRepository) {
        this.addressSearchRepository = addressSearchRepository;
        this.addressRepository = addressRepository;
    }

    // search for a specific address. If country parameter provided, will only search results
    // of that country.
    @GetMapping("/search")
    public List<AddressModel> searchAddresses(@RequestBody String addressSearchTerms,
                                              @RequestParam(value = "country", required = false)
                                                      String country) {

        // results to be obtained
        Iterable<AddressSearchModel> searchResults;

        // obtain search results from Elasticsearch
        if (country != null) {
            searchResults = executeSearchQuery(addressSearchTerms, country);
        } else { // no country param provided
            searchResults = executeSearchQuery(addressSearchTerms);
        }

        // obtain mongoDBId's from search results
        List<String> mongoIdList = new LinkedList<>();
        for (AddressSearchModel model : searchResults) {
            mongoIdList.add(model.mongoDBId);
        }

        // obtain addressModels from addressRepository
        Iterable<AddressModel> results = addressRepository.findAllById(mongoIdList);

        // return Address Models
        return (List<AddressModel>)results;
    }

    private Iterable<AddressSearchModel> executeSearchQuery(String fullAddress) {

        // construct query
        BoolQueryBuilder query = QueryBuilders.boolQuery()
                .filter(QueryBuilders.fuzzyQuery("fullAddress", fullAddress));

        return addressSearchRepository.search(query);
    }

    private Iterable<AddressSearchModel> executeSearchQuery(String fullAddress, String country) {

            // construct query
            BoolQueryBuilder query = QueryBuilders.boolQuery()
                    .filter(QueryBuilders.matchQuery("country", country))
                    .filter(QueryBuilders.fuzzyQuery("fullAddress", fullAddress));

           return addressSearchRepository.search(query);
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
