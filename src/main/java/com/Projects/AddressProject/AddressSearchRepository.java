package com.Projects.AddressProject;

import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface AddressSearchRepository extends ElasticsearchRepository<SearchModel, String> {

}

