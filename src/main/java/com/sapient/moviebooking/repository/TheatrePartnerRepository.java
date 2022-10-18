package com.sapient.moviebooking.repository;

import com.sapient.moviebooking.entity.TheatrePartner;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "theatrePartners", path = "theatrePartners")
public interface TheatrePartnerRepository extends PagingAndSortingRepository<TheatrePartner, Integer> {
}
