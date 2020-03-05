package com.Projects.AddressProject;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface AddressSearchRepository extends ElasticsearchRepository<AddressSearchModel, String> {
    List<AddressSearchModel> findByfullAddressLike(String fullAddress);
    List<AddressSearchRepository> findByfullAddressLikeAndCountry(String fullAddress, String Country);
}
