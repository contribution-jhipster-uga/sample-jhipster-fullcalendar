package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.CalendarProvider;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link CalendarProvider} entity.
 */
public interface CalendarProviderSearchRepository extends ElasticsearchRepository<CalendarProvider, Long> {
}
