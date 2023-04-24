package backend.backend.repositories;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import backend.backend.models.ExpenseAccount;
import backend.backend.models.UserLogin;
import static backend.backend.repositories.Queries.*;


@Repository
public class SQLRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<UserLogin> findUserByEmail(String email){
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_FIND_BY_EMAIL, email);

        if (!rs.next()) 
            return Optional.empty();

        UserLogin user = UserLogin.createUser(rs);

        return Optional.of(user);
    }

    public void registerUser(UserLogin user){
        String userId = UUID.randomUUID().toString().substring(0,8);
        jdbcTemplate.update(SQL_REGISTER_USER, userId,  user.getUsername(), user.getEmail(), user.getPassword(), user.getRole().name());
    }

    public String createGroup(String email, String groupName){
        String groupID = UUID.randomUUID().toString().substring(0,8);
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_FIND_BY_EMAIL, groupName);
        if (rs.next()) 
            return "Duplicate Group";
        else{
            Integer rowsAffected = jdbcTemplate.update(SQL_CREATE_GROUP, groupID, groupName, email);
            return rowsAffected.toString();
        }
    }

    public List<ExpenseAccount> getGroupList(String email){

		List<ExpenseAccount> groupList = new LinkedList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_FIND_GROUPS, email);

        while (rs.next()) {
            ExpenseAccount acct = ExpenseAccount.createFromDoc(rs);
            groupList.add(acct);
        }
        return groupList;
    }
}
