package dao;

import model.Person;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Victoria Zhirnova
 * @project PersonMVC
 */

@Component
public class PersonDbDAO {
    private static String URL="jdbc:postgresql://localhost:5432/first_db";
    private static String USERNAME="postgres";
    private static String PASSWORD="2104V";
    private static Connection connection;
    static {
        try{
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException exception){
            exception.printStackTrace();
        }
        try{
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException exception){
            exception.printStackTrace();
        }
    }
    public List<Person> index(){
        List<Person> people =  new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM person";
            ResultSet resultSet = statement.executeQuery(SQL);
            while(resultSet.next()) {
                Person person = new Person();
                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setEmail(resultSet.getString("email"));
                people.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return people;
    }
    public void save(Person person){
        try {
          /*
          Statement statement =  connection.createStatement();
          String SQL = "INSERT INTO Person VALUES(" + 1 + ",'" + person.getName() + "'," + person.getAge() + ",'" +
          person.getEmail() + "')"; statement.executeUpdate(SQL);
          */
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO person VALUES(1, ?, ?, ?)");
            preparedStatement.setString(1, person.getName());
            preparedStatement.setInt(2, person.getAge());
            preparedStatement.setString(3, person.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void update(int id, Person updatedPerson) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE person SET name=?, age=?, email=? WHERE id=?");
            preparedStatement.setString(1, updatedPerson.getName());
            preparedStatement.setInt(2, updatedPerson.getAge());
            preparedStatement.setString(3, updatedPerson.getEmail());
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Person personToBeUpdated = show(id);
        // personToBeUpdated.setName(updatedPerson.getName());
        // personToBeUpdated.setAge(updatedPerson.getAge());
        // personToBeUpdated.setEmail(updatedPerson.getEmail());
    }
    public void delete(int id){
        // people.removeIf(p -> p.getId()==id);
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("DELETE  FROM person WHERE id=?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public Person show(int id){
        //находим человека с id из List создадим stream исп filter для отфильтровывания person по id, если нет - null
        // return people.stream().filter(person
        //     -> person.getId() == id).findAny().orElse(null);
        Person person = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT *  FROM person WHERE id=?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            person = new Person();
            person.setId(resultSet.getInt("id"));
            person.setName(resultSet.getString("name"));
            person.setEmail(resultSet.getString("email"));
            person.setAge(resultSet.getInt("age"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return person;
    }
}
