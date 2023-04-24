package backend.backend.models;

import java.util.Date;
import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;

public class Transactions {
    private String email;
    private String groupName;
    private String description;
    private String category;
    private Double amount;
    private Date date;
    private String transactionID;

    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getTransactionID() {
        return transactionID;
    }
    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }


    public static Transactions createfromJsonObj(JsonObject jsonObj) {
        Transactions transaction = new Transactions();
        Date d = new Date();

        transaction.setAmount(jsonObj.getJsonNumber("amount").doubleValue());
		transaction.setCategory(jsonObj.getString("category"));
		transaction.setGroupName(jsonObj.getString("restaurant_id"));
		transaction.setDescription(jsonObj.getString("description"));
		transaction.setDate(d);
		transaction.setEmail(jsonObj.getString("email"));
		transaction.setTransactionID(jsonObj.getString("transactionID"));

        return transaction;
    }

    public static Transactions createfromDoc(Document doc) {
		Transactions txn = new Transactions();

        txn.setAmount(doc.getDouble("amount"));
		txn.setCategory(doc.getString("category"));
		txn.setGroupName(doc.getString("groupName"));
		txn.setDescription(doc.getString("description"));
		txn.setDate(doc.getDate("date"));
		txn.setEmail(doc.getString("email"));
		txn.setTransactionID(doc.getString("transactionID"));
		return txn;
	}

    public JsonObject toJson() {
		JsonObjectBuilder json=  Json.createObjectBuilder()
						.add("amount", amount)
						.add("category", category)
						.add("groupName", groupName)
						.add("description", description)
						.add("date", date.toString())
						.add("email", email)
						.add("transactionID", transactionID);
		return json.build();
	}

}
