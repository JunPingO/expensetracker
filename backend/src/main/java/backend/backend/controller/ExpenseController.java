package backend.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend.backend.models.ExpenseAccount;
import backend.backend.models.Transactions;
import backend.backend.models.UserLogin;
import backend.backend.services.DatabaseService;
import backend.backend.services.MailService;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

@RestController
@RequestMapping("/api")
public class ExpenseController {
    
    @Autowired
    private DatabaseService databaseSvc;

    @Autowired
    private MailService mailSvc;


    @PostMapping(path = "/testpath")
    public ResponseEntity<String> testPath(){

        JsonObject message = Json.createObjectBuilder()
            .add("message", "Test Message sent!")
            .build();

        return ResponseEntity.ok().body(message.toString());
    }

    @GetMapping(path = "/testpath")
    public ResponseEntity<String> testGetPath(){

        JsonObject message = Json.createObjectBuilder()
            .add("message", "Test Message sent!")
            .build();

        return ResponseEntity.ok().body(message.toString());
    }

    @PostMapping(path = "/insertgroup")
    public ResponseEntity<String> insertGroup(@RequestBody ExpenseAccount expAcct){

        String result = databaseSvc.insertGroup(expAcct.getEmail(), expAcct.getGroupName());

        JsonObject message = Json.createObjectBuilder()
            .add("message", result)
            .build();

        return ResponseEntity.ok().body(message.toString());
    }

    @GetMapping(path = "/getgroups/{email}")
    public ResponseEntity<String> getGroups(@PathVariable String email){


        List<ExpenseAccount> groupList = databaseSvc.getGroups(email);

        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (ExpenseAccount acct: groupList){
            arrBuilder.add(acct.toJson());
        }

        return ResponseEntity.ok().body(arrBuilder.build().toString());
    }

    @PostMapping(path = "/addtransaction")
    public ResponseEntity<String> addTransaction(@RequestBody Transactions txn){

        databaseSvc.addTransaction(txn);
        JsonObject message = Json.createObjectBuilder()
            .add("message", "success")
            .build();

        return ResponseEntity.ok().body(message.toString());
    }

    @GetMapping(path = "/getcategories")
    public ResponseEntity<String> getDistinctCategories(){

        // databaseSvc.addTransaction(txn);

        return ResponseEntity.ok().body("test");
    }

    @GetMapping(path = "/gettransactions")
    public ResponseEntity<String> getAllTransactions(@RequestParam String email, @RequestParam String groupname){

        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        System.out.println(">>>>>>>" + email + groupname);

        databaseSvc.getAllTransactionsByGroupAndEmail(email, groupname).stream()
        .forEach(txn -> {
            arrBuilder.add(txn.toJson());
        });
        return ResponseEntity.ok().body(arrBuilder.build().toString());
    }

    @DeleteMapping(path = "/deletetransaction")
    public ResponseEntity<String> deleteTransaction(@RequestParam String transactionID){

        databaseSvc.deleteTransaction(transactionID);
        JsonObject message = Json.createObjectBuilder()
            .add("message", "deleted!")
            .build();

        return ResponseEntity.ok().body(message.toString());
    }

    @PostMapping(path = "/upload")
    public ResponseEntity<String> upload(@RequestBody Transactions txn){

        databaseSvc.addTransaction(txn);
        JsonObject message = Json.createObjectBuilder()
            .add("message", "success")
            .build();

        return ResponseEntity.ok().body(message.toString());
    }

    @GetMapping(path = "/email")
    public ResponseEntity<String> email(@RequestParam String email){

        // databaseSvc.addTransaction(txn);
        // JsonObject message = Json.createObjectBuilder()
        //     .add("message", "success")
        //     .build();
        
        String subject = "ExpenseTracker Register success";
        String message = "Thank you for the support!";

        mailSvc.sendEmail(email, subject, message);

        return ResponseEntity.ok().body(message.toString());
    }

}
