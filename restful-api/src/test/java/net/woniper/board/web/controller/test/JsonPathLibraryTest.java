package net.woniper.board.web.controller.test;

import com.jayway.jsonpath.JsonPath;
import net.woniper.board.domain.type.AuthorityType;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
* Created by woniper on 15. 2. 1..
*/
public class JsonPathLibraryTest {

    String json = "{\n" +
            "    \"store\": {\n" +
            "        \"book\": [\n" +
            "            {\n" +
            "                \"category\": \"reference\",\n" +
            "                \"author\": \"Nigel Rees\",\n" +
            "                \"title\": \"Sayings of the Century\",\n" +
            "                \"price\": 8.95\n" +
            "            },\n" +
            "            {\n" +
            "                \"category\": \"fiction\",\n" +
            "                \"author\": \"Evelyn Waugh\",\n" +
            "                \"title\": \"Sword of Honour\",\n" +
            "                \"price\": 12.99\n" +
            "            },\n" +
            "            {\n" +
            "                \"category\": \"fiction\",\n" +
            "                \"author\": \"Herman Melville\",\n" +
            "                \"title\": \"Moby Dick\",\n" +
            "                \"isbn\": \"0-553-21311-3\",\n" +
            "                \"price\": 8.99\n" +
            "            },\n" +
            "            {\n" +
            "                \"category\": \"fiction\",\n" +
            "                \"author\": \"J. R. R. Tolkien\",\n" +
            "                \"title\": \"The Lord of the Rings\",\n" +
            "                \"isbn\": \"0-395-19395-8\",\n" +
            "                \"price\": 22.99\n" +
            "            }\n" +
            "        ],\n" +
            "        \"bicycle\": {\n" +
            "            \"color\": \"red\",\n" +
            "            \"price\": 19.95\n" +
            "        }\n" +
            "    },\n" +
            "    \"expensive\": 10\n" +
            "}";

    @Test
    public void test_get_authors() throws Exception {
            // json에서 author만 List<String>으로 만들기
            List<String> authors = JsonPath.read(json, "$.store.book[*].author");
            assertEquals(authors.size(), 4);

            String firstCategory = JsonPath.read(json, "$.store.book[0].category");
            assertEquals(firstCategory, "reference");

//        Object document = Configuration.defaultConfiguration().getProvider().parse(json);
//        System.out.println(document);

            Long a = 1L;
            Long b = 2L;

//        assertTrue(a.equals(b));
            assertTrue(a.compareTo(b) < 0);

    }

    @Test
    public void testName() throws Exception {
        System.out.println(AuthorityType.ADMIN);
    }
}
