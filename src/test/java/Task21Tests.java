import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Task21Tests {
    private static final String baseUrl = "https://qa-internship.avito.com/api/1/";
    private static final int sellerId = 284191;
    private static final List<String> ids = new ArrayList<>();

    @BeforeAll
    public static void postTestOK() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        for (int i = 30; i < 33; i++) {
            Item curItem = new Item(null, null, "Item " + i, i, sellerId,
                    new Statistics(i, i, i));
            String json = objectMapper.writeValueAsString(curItem);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "item"))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            String id = response.body().split(" ")[3];
            id = id.substring(0, id.length() - 3);
            ids.add(id);
        }
    }

    @Test
    public void getAdByIdTestOK() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "item/" + ids.get(0)))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<Item>> typeReference = new TypeReference<List<Item>>(){};
        List<Item> list = objectMapper.readValue(response.body(), typeReference);
        Statistics expectedStat = new Statistics(30, 30, 30);
        Item expected = new Item(null,
                ids.get(0),
                "dsdsd",
                30,
                sellerId,
                expectedStat);
        assertEquals(expected, list.get(0));
    }

    @Test
    public void getAdByIdTestNotFound() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "item/fb789582-f132-4b16-a3a2-1adc6738ffb3"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void getAdByIdTestBadRequest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "item/1adc6738ffb3"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    public void postTestBadRequest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        String json = "{\n" +
                "  \"name\" : \"Item0\",\n" +
                "  \"price\" : \"0\",\n" +
                "  \"sellerId\" : 284195,\n" +
                "  \"statistics\" : {\n" +
                "    \"contacts\" : 0,\n" +
                "    \"likes\" : 0,\n" +
                "    \"viewCount\" : 0\n" +
                "  }\n" +
                "}";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "item"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    public void statByIdTestOK() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "statistic/" + ids.get(0)))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<Statistics>> typeReference = new TypeReference<List<Statistics>>(){};
        List<Statistics> list = objectMapper.readValue(response.body(), typeReference);
        Statistics expectedStat = new Statistics(30, 60, 90);
        assertEquals(expectedStat, list.get(0));
    }

    @Test
    public void statByIdTestNotFound() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "statistic/0cd4183f"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    public void adBySellerIdTestOK() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + sellerId + "/item"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<Item>> typeReference = new TypeReference<List<Item>>(){};
        List<Item> list = objectMapper.readValue(response.body(), typeReference);

        for (int i = 30; i < 33; i++) {
            Statistics expectedStat = new Statistics(i, i, i);
            Item expected = new Item(null,
                    "dsdsd",
                    ids.get(i - 30),
                    i,
                    sellerId,
                    expectedStat);
            assertTrue(list.contains(expected));
        }
    }
}
@JsonInclude(JsonInclude.Include.NON_NULL)
class Statistics {
    int contacts;
    int likes;
    int viewCount;

    public Statistics(int contacts, int likes, int viewCount) {
        this.contacts = contacts;
        this.likes = likes;
        this.viewCount = viewCount;
    }

    public Statistics() {}

    public int getContacts() {
        return contacts;
    }

    public int getLikes() {
        return likes;
    }

    public int getViewCount() {
        return viewCount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Statistics that = (Statistics) o;
        return contacts == that.contacts && likes == that.likes && viewCount == that.viewCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(contacts, likes, viewCount);
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
class Item {
    @JsonIgnore
    String createdAt;
    String id;
    String name;
    long price;
    long sellerId;
    Statistics statistics;

    public Item(String createdAt, String id, String name, long price, long sellerId, Statistics statistics) {
        this.createdAt = createdAt;
        this.id = id;
        this.name = name;
        this.price = price;
        this.sellerId = sellerId;
        this.statistics = statistics;
    }

    public Item(){};

    public String getCreatedAt() {
        return createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public long getSellerId() {
        return sellerId;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return price == item.price
                && sellerId == item.sellerId
                && Objects.equals(createdAt, item.createdAt)
                && Objects.equals(id, item.id)
                && Objects.equals(name, item.name)
                && Objects.equals(statistics, item.statistics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, id, name, price, sellerId, statistics);
    }
}
