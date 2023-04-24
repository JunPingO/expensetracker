package backend.backend.models;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class ExpenseAccount {
    private String groupName;
    private String email;
    
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public JsonObject toJson(){
		return Json.createObjectBuilder()
			.add("name",getEmail())
			.add("groupName",getGroupName())
			.build();
	}

    public static ExpenseAccount createFromDoc (SqlRowSet rs) {
		ExpenseAccount acct = new ExpenseAccount();
		acct.setEmail(rs.getString("email"));
		acct.setGroupName(rs.getString("expensegroup_name"));
		return acct;
	}
}
