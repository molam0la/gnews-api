package com.molam0la.dev.gnews_api.cassandra.repository;

import com.molam0la.dev.gnews_api.cassandra.model.DBArticle;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DBArticleRepository extends CassandraRepository<DBArticle, Id> {

    @Query("SELECT * FROM gnews_api.dbarticle")
    Iterable<DBArticle> findAllArticles();

    @Query("SELECT * FROM gnews_api.dbarticle WHERE topic=?0 limit 10")
    Iterable <DBArticle> findAllArticlesByTopic(String topic);

}
