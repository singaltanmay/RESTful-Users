package com.example.restfulusers.dao;

import com.example.restfulusers.model.User;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgreSQL")
public class PostgresDbAccessService implements UserDao {

    private final String DB_IP_ADDRESS = "localhost";
    private final String DB_PORT_NUMBER = "5432";
    private final String DB_NAME = "testdb";
    private final String USERNAME = "postgres";
    private final String PASSWORD = "password";

    private final String TABLE_NAME = "user_info";

    enum COLUMN_NAMES {
        UUID,
        FIRST_NAME,
        LAST_NAME,
        PHONE_NUMBER
    }

    private Connection connection;

    public PostgresDbAccessService() {
        initConnection();
        if (!isTablePresent()) {
            createTable();
        }
    }

    private void initConnection() {

        if (this.connection != null) return;

        try {
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection("jdbc:postgresql://"
                            + DB_IP_ADDRESS
                            + ":"
                            + DB_PORT_NUMBER + "/" + DB_NAME,
                    USERNAME, PASSWORD);

            System.out.println("Connected to database");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean isTablePresent() {

        String query = " SELECT table_name FROM information_schema.tables WHERE table_name = '" +
                TABLE_NAME +
                "';";

        Statement statement = null;

        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    private void createTable() {

        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAMES.UUID + " CHAR(36) PRIMARY KEY NOT NULL, " +
                COLUMN_NAMES.FIRST_NAME + " VARCHAR(20), " +
                COLUMN_NAMES.LAST_NAME + " VARCHAR(20), " +
                COLUMN_NAMES.PHONE_NUMBER + " VARCHAR(20)" +
                ");";

        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public boolean insertNewUser(UUID uuid, User user) {
        user.setUUID(uuid);

        boolean flag;

        String sql = "INSERT INTO " +
                TABLE_NAME +
                " (" +
                COLUMN_NAMES.UUID + ", " +
                COLUMN_NAMES.FIRST_NAME + ", " +
                COLUMN_NAMES.LAST_NAME + ", " +
                COLUMN_NAMES.PHONE_NUMBER +
                ") VALUES (" +
                "'" + user.getUUID().toString() + "'" + "," +
                "'" + user.getFirstName() + "'" + "," +
                "'" + user.getLastName() + "'" + "," +
                "'" + user.getPhoneNumber() + "'" +
                ");";

        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);
            flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
            flag = false;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return flag;
    }

    @Override
    public List<User> getAllUsers() {

        List<User> list = new LinkedList<>();

        String sql = "SELECT * FROM " + TABLE_NAME + ";";

        Statement statement = null;

        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                do {
                    UUID uuid = UUID.fromString(resultSet.getString(String.valueOf(COLUMN_NAMES.UUID)));
                    String firstName = resultSet.getString(String.valueOf(COLUMN_NAMES.FIRST_NAME));
                    String lastName = resultSet.getString(String.valueOf(COLUMN_NAMES.LAST_NAME));
                    String phoneNumber = resultSet.getString(String.valueOf(COLUMN_NAMES.PHONE_NUMBER));

                    list.add(new User(uuid, firstName, lastName, phoneNumber));
                } while (resultSet.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return list;
    }

    @Override
    public Optional<User> getUserByID(UUID uuid) {

        Optional<User> user = Optional.empty();

        String sql = "SELECT * FROM " +
                TABLE_NAME +
                " WHERE " +
                COLUMN_NAMES.UUID +
                " = '" +
                uuid.toString() +
                "';";

        Statement statement = null;

        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                String firstName = resultSet.getString(String.valueOf(COLUMN_NAMES.FIRST_NAME));
                String lastName = resultSet.getString(String.valueOf(COLUMN_NAMES.LAST_NAME));
                String phoneNumber = resultSet.getString(String.valueOf(COLUMN_NAMES.PHONE_NUMBER));

                user = Optional.of(new User(uuid, firstName, lastName, phoneNumber));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return user;
    }

    @Override
    public boolean deleteAllUsers() {

        boolean flag;

        String sql = "DELETE FROM " + TABLE_NAME + ";";

        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);
            flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
            flag = false;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return flag;
    }

    @Override
    public byte deleteUserByID(UUID uuid) {

        byte flag;

        String sql = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_NAMES.UUID + " = '" + uuid.toString() + "';";

        Statement statement = null;

        try {

            if (userExistsAtID(uuid)) {

                statement = connection.createStatement();
                statement.executeUpdate(sql);
                flag = UserDao.OK;

            } else flag = UserDao.RESOURCE_NOT_FOUND;

        } catch (SQLException e) {
            e.printStackTrace();
            flag = UserDao.BAD_REQUEST;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return flag;
    }

    @Override
    public byte replaceOrInsertUserByID(UUID uuid, User user) {

        if (uuid == null) return UserDao.BAD_REQUEST;

        if (userExistsAtID(uuid)) deleteUserByID(uuid);

        boolean b = insertNewUser(uuid, user);
        return b ? UserDao.OK : UserDao.RESOURCE_NOT_FOUND;
    }

    @Override
    public byte updateUserByID(UUID uuid, User user) {

        byte flag = UserDao.BAD_REQUEST;

        if (!userExistsAtID(uuid)) return UserDao.RESOURCE_NOT_FOUND;

        byte deltaCount = 0;

        StringBuilder builder = new StringBuilder();

        String first_name = user.getFirstName();
        if (validString(first_name)) {
            builder.append(COLUMN_NAMES.FIRST_NAME + " = '").append(first_name).append("' ");
            deltaCount++;
        }

        String last_name = user.getLastName();
        if (validString(last_name)) {
            if (deltaCount > 0) builder.append(", ");
            builder.append(COLUMN_NAMES.LAST_NAME + " = '").append(last_name).append("' ");
            deltaCount++;
        }

        String phone_number = user.getPhoneNumber();
        if (validString(phone_number)) {
            if (deltaCount > 0) builder.append(", ");
            builder.append(COLUMN_NAMES.PHONE_NUMBER + " = '").append(phone_number).append("' ");
            deltaCount++;
        }

        if (deltaCount > 0) {
            String sql = "UPDATE " +
                    TABLE_NAME +
                    " SET " +
                    builder.toString()
                    + "WHERE " +
                    COLUMN_NAMES.UUID +
                    " = '" +
                    uuid +
                    "'";


            Statement statement = null;

            try {
                statement = connection.createStatement();
                statement.executeUpdate(sql);
                flag = UserDao.OK;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return flag;
    }

    private boolean userExistsAtID(UUID uuid) {

        boolean flag;

        String doesExist = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COLUMN_NAMES.UUID + " = '" + uuid.toString() + "';";

        Statement statement = null;

        try {
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(doesExist);
            resultSet.next();

            int numResults = resultSet.getInt(1);

            flag = numResults != 0;

        } catch (SQLException e) {
            e.printStackTrace();
            flag = false;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return flag;

    }

    private boolean validString(String string) {
        return (string != null) && (!string.isBlank());
    }

}
