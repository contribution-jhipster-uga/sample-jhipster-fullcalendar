package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.CalendarEvent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link CalendarEvent} entity.
 */
public interface CalendarEventSearchRepository extends ElasticsearchRepository<CalendarEvent, Long> {
}
