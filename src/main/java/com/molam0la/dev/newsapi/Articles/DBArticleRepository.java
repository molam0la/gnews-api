package com.molam0la.dev.newsapi.Articles;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DBArticleRepository {

    private static final String TABLE_NAME = "articles";

    private Session session;

    public DBArticleRepository(Session session) {
        this.session = session;
    }

    public void createTable() {
        String query =
                "CREATE TABLE articles (" +
                        "id int, " +
                        "topic text, " +
                        "title text, " +
                        "description text, " +
                        "url text, " +
                        "published_at timestamp, " +
                        "source text, " +
                        "source_url text, " +
                        "PRIMARY KEY (topic, published_at)) " +
                        "WITH CLUSTERING ORDER BY (published_at DESC);";

        session.execute(query);
    }

    public void insertArticle(DBArticle DBArticle) {
        String query = "INSERT into articles (id, published_at, topic, description, source, source_url, title, url) " +
                "values (" +
                DBArticle.getId() + ", " +
                "'" + DBArticle.getPublished_at() + "', " +
                "'" + DBArticle.getTopic() + "', " +
                "'" + DBArticle.getDescription() + "', " +
                "'" + DBArticle.getSource() + "', " +
                "'" + DBArticle.getSource_url() + "', " +
                "'" + DBArticle.getTitle() + "', " +
                "'" + DBArticle.getUrl() + "');";
        session.execute(query);
    }

    public List<DBArticle> selectAllCachedArticles() {
        String query = "SELECT * FROM gnews_api.articles;";
        List<Row> result = session.execute(query).all();

            return result.stream().map(row -> new DBArticle(
                    row.getInt("id"),
                    row.getString("topic"),
                    row.getString("title"),
                    row.getString("description"),
                    row.getString("url"),
                    row.getTimestamp("published_at").toInstant(),
                    row.getString("source"),
                    row.getString("source_url"))).collect(Collectors.toList());
    }

    public void deleteTable(String tableName) {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        session.execute(query);
    }
}
