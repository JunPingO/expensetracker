package backend.backend.repositories;

public class Queries {
    public static final String SQL_FIND_BY_EMAIL = "select * from users where email=?";
    public static final String SQL_REGISTER_USER = "insert into users(user_id, username, email, password, role) values (?,?,?,?,?)";
    public static final String SQL_CREATE_GROUP = "insert into expensegroup(expensegroup_id, expensegroup_name, email) values (?,?,?)";
    public static final String SQL_FIND_GROUPS = "select * from expensegroup where email=?";
    public static final String SQL_FIND_GROUP = "select * from expensepjt.expensegroup where expensegroup_name = ?";

}
