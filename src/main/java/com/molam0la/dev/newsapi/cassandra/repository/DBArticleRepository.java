package com.molam0la.dev.newsapi.cassandra.repository;

import com.molam0la.dev.newsapi.cassandra.model.DBArticle;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DBArticleRepository extends CassandraRepository<DBArticle, Id> {

    @Query("SELECT * FROM gnews_api.dbarticle")
    Iterable<DBArticle> findAllArticles();

    @Query("SELECT * FROM gnews_api.dbarticle WHERE topic='world' limit 10")
    Iterable<DBArticle> findWorldArticles();

    @Query("SELECT * FROM gnews_api.dbarticle WHERE topic='technology' limit 10")
    Iterable<DBArticle> findTechnologyArticles();

//    public void createTable() {
//        String query =
//                "CREATE TABLE articles (" +
//                        "id int, " +
//                        "topic text, " +
//                        "title text, " +
//                        "description text, " +
//                        "url text, " +
//                        "published_at timestamp, " +
//                        "source text, " +
//                        "source_url text, " +
//                        "PRIMARY KEY (topic, published_at)) " +
//                        "WITH CLUSTERING ORDER BY (published_at DESC);";
//
//        session.execute(query);
//    }
}
