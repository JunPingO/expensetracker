package backend.backend.services;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.backend.models.ExpenseAccount;
import backend.backend.models.Transactions;
import backend.backend.models.UserLogin;
import backend.backend.repositories.MongoRepository;
import backend.backend.repositories.SQLRepository;

@Service
public class DatabaseService {
    @Autowired
    private SQLRepository sqlRepository;
    @Autowired
    private MongoRepository mongoRepo;

    
    public Optional<UserLogin> findUserByEmail(String email) {
        return sqlRepository.findUserByEmail(email);
    }

    public void registerUser(UserLogin user) {
        sqlRepository.registerUser(user);
    }

    @Transactional
    public String insertGroup(String email, String groupName) {
        // sqlRepository.findUserByEmail(email);
        String result = sqlRepository.createGroup(email, groupName);
        if (result.equals("Duplicate Group")){
            return result;
        } else {
            return "Successfully created" + groupName;
        }
    }

    public List<ExpenseAccount> getGroups(String email) {
        return sqlRepository.getGroupList(email);
    }

    public void addTransaction(Transactions txn) {
        mongoRepo.addTransaction(txn);
    }

    public List<Transactions> getAllTransactionsByGroupAndEmail(String email, String groupName) {
        return mongoRepo.getAllTransactions(groupName, email);

    }

    // public List<Document> getDistinctCategories(String groupName) {
    //     mongoRepo.addTransaction(txn);
    // }
}
