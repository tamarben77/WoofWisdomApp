package HandleForumsRequests;

import ManagmentDB.MySQLConnector;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jcraft.jsch.JSchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import java.util.stream.Collectors;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static ManagmentDB.MySQLConnector.insertNewRow;

@RestController
@RequestMapping("/dogForums")
public class Forums {

    @PostMapping("/createNewQuery")
    public void createNewQuery(@RequestBody String json /*@RequestBody ForumQuery query*/) {
        Gson gson = new Gson();
        ForumQuery query = gson.fromJson(json, ForumQuery.class);
        System.out.println(json.toString());
        System.out.println(query.getIfNewQuery()+" "+query.getCategory());
        String tableName = "forums";
        String[] columnNames = {"ifNewQuery",  "questionTitle", "questionDetails", "userID", "userType",  "upvotes", "views", "category"};
        Object[] values = {query.getIfNewQuery(), query.getQuestionTitle(), query.getQuestionDetails(), query.getUserID(), query.getUserType(), query.getUpvotes(), query.getViews(), query.getCategory()};
        insertNewRow(tableName, columnNames, values);
    }


    @GetMapping("/showAllForumsPost")
    public ResponseEntity showAllForumsPost() throws SQLException, JsonProcessingException {
        String json = getTableDataJson("forums");
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @GetMapping("/showCommentsOfPost")
    public static ResponseEntity showCommentsOfPost(String questionId) throws SQLException, JsonProcessingException, JSchException {
        List<Map<String, Object>> data = MySQLConnector.select("comments","questionId",questionId);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // add the JSR-310 module
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // configure to write dates as ISO-8601 strings
        return new ResponseEntity<>(mapper.writeValueAsString(data), HttpStatus.OK);
    }

    @PostMapping("/createNewComment")
    public void createNewComment(@RequestBody String json /*@RequestBody ForumQuery query*/) {
        Gson gson = new Gson();
        CommentQuery query = gson.fromJson(json, CommentQuery.class);
        System.out.println(json.toString());
        System.out.println(query.getCommentTitle()+" "+query.getUserID());
        String tableName = "comments";
        String[] columnNames = {"commentTitle",  "commentDesc", "userName", "userID", "questionID"};
        Object[] values = {query.getCommentTitle(), query.getCommentDesc(), query.getUserName(), query.getUserID(), query.getQuestionID()};
        insertNewRow(tableName, columnNames, values);
        MySQLConnector.incrementCommentCountForQuestion(query.getQuestionID());
    }

    public static String getTableDataJson(String tableName) throws JsonProcessingException {
        List<Map<String, Object>> data = MySQLConnector.getTable(tableName);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // add the JSR-310 module
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // configure to write dates as ISO-8601 strings
        return mapper.writeValueAsString(data);
    }

    @GetMapping("/getCommentCountsForAllQuestions")
    public ResponseEntity<Map<String, Integer>> getCommentCountsForAllQuestions() {
        try {
            Map<Integer, Integer> originalCommentCounts = MySQLConnector.getCommentCountsForAllQuestions();

            // Convert the keys from Integer to String
            Map<String, Integer> stringKeyedCommentCounts = originalCommentCounts.entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            entry -> entry.getKey().toString(), // Convert key to String
                            Map.Entry::getValue
                    ));

            return new ResponseEntity<>(stringKeyedCommentCounts, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
