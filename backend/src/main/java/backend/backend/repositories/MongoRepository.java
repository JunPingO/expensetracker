package backend.backend.repositories;

import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import backend.backend.models.Transactions;

@Repository
public class MongoRepository {
    
    @Autowired
    private MongoTemplate mongoTemplate;

    public static final String MONGO_DB = "gmailstore";

    public List<Document> getTokensByUser(String username){

        // Create a criteria/predicate
        Criteria c = Criteria.where("username").is(username);

        // Query to use the criteria
        Query q = Query.query(c);

        return mongoTemplate.find(q, Document.class, MONGO_DB);
    }

    public String addTokensByUser(String username, String accessToken, String refreshToken){

        Criteria c = Criteria.where("username").is(username);

        // Query to use the criteria
        Query q = Query.query(c);

        Update updateOps = new Update()
            .set("username", username)
            .set("access_token", accessToken)
            .set("refresh_token", refreshToken);

        mongoTemplate.upsert(q, updateOps, MONGO_DB);

        return accessToken;
        
    }

    public void addTransaction(Transactions transaction) {
        String txnID = UUID.randomUUID().toString().substring(0,8);
        transaction.setTransactionID(txnID);
		mongoTemplate.insert(transaction, "expensepjt");
	}

    public List<Transactions> getAllTransactions(String groupName, String email) {

        Criteria c1 = Criteria.where("groupName").is(groupName);
        Criteria collatedCriteria = c1.andOperator(Criteria.where("email").is(email));

        Query q = Query.query(collatedCriteria);

        return mongoTemplate.find(q, Document.class, "expensepjt")
            .stream()
            .map( d -> Transactions.createfromDoc(d))
            .toList();
	}

    public List<Document> getDistinctCategories(String groupName) {

        Criteria c = Criteria.where("groupName").is(groupName);

        Query q = Query.query(c);

        return mongoTemplate.findDistinct(q, "category", "expensepjt", Document.class);
	}

    public void deleteTransaction(String transactionID) {
        // Create a criteria/predicate
        Criteria c = Criteria.where("transactionID").is(transactionID);

        // Query to use the criteria
        Query q = Query.query(c);

        DeleteResult dr = mongoTemplate.remove(q, "expensepjt");
        System.out.println(dr);
        
	}
    
}
