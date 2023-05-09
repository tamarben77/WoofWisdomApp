package HandleForumsRequests;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static ManagmentDB.MySQLConnector.insertNewRow;

@RestController
@RequestMapping("/dogForums")
public class Forums {

    @PostMapping("/createNewQuery")
    public void createNewQuery(@RequestBody ForumQuery query) {
        String tableName = "forums";
        String[] columnNames = {"IfNewQuery", "questionID", "questionTitle", "questionDetails", "userID", "userType", "dateandTime", "upvotes", "views", "category"};
        String[] values = {query.getIfNewQuery(), query.getQuestionID(), query.getQuestionTitle(), query.getQuestionDetails(), query.getUserID(), query.getUserType(), query.getDateandTime(), query.getUpvotes(), query.getViews(), query.getCategory()};
        insertNewRow(tableName, columnNames, values);
    }

}