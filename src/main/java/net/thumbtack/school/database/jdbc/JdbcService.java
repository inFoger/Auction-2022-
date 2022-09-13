package net.thumbtack.school.database.jdbc;

import net.thumbtack.school.database.model.Group;
import net.thumbtack.school.database.model.School;
import net.thumbtack.school.database.model.Subject;
import net.thumbtack.school.database.model.Trainee;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcService {
    //1. Добавляет Trainee в базу данных.
    public static void insertTrainee(Trainee trainee) throws SQLException {
        String insertQuery = "insert into trainee values(?,?,?,?,null)";
        int result;
        try (PreparedStatement stmt = JdbcUtils.getConnection().prepareStatement(insertQuery, com.mysql.jdbc.Statement.RETURN_GENERATED_KEYS)) {
            stmt.setNull(1, java.sql.Types.INTEGER);
            stmt.setString(2, trainee.getFirstName());
            stmt.setString(3, trainee.getLastName());
            stmt.setInt(4, trainee.getRating());
            result = stmt.executeUpdate();
            if (result > 0) {
                try(ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        trainee.setId(rs.getInt(1));
                        rs.close();
                    }
                }
            }
        }


    }

    //2. Изменяет ранее записанный Trainee в базе данных. В случае ошибки выбрасывает SQLException.
    public  static void updateTrainee(Trainee trainee) throws SQLException {
        String updateQuery = "update trainee set firstname = ?, lastname = ?, rating = ? where id = ?";
        try (PreparedStatement stmt = JdbcUtils.getConnection().prepareStatement(updateQuery)) {
            stmt.setString(1, trainee.getFirstName());
            stmt.setString(2, trainee.getLastName());
            stmt.setInt(3, trainee.getRating());
            stmt.setInt(4, trainee.getId());
            stmt.executeUpdate();
        }
    }

    //3. Получает Trainee из базы данных по его ID, используя метод получения “по именам полей”.
    // Если Trainee с таким ID нет, возвращает null.
    public static Trainee getTraineeByIdUsingColNames(int traineeId) throws SQLException {
        String selectQuery = "select * from trainee where id = ?";
        try (PreparedStatement stmt = JdbcUtils.getConnection().prepareStatement(selectQuery)) {
            stmt.setInt(1, traineeId);
            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    String firstName = rs.getString("firstName");
                    String lastName = rs.getString("lastName");
                    int rating = rs.getInt("rating");
                    return new Trainee(traineeId, firstName, lastName, rating);
                }
            }
            return null;
        }
    }

    //4. Получает Trainee из базы данных по его ID, используя метод получения “по номерам полей”.
    // Если Trainee с таким ID нет, возвращает null.
    public static Trainee getTraineeByIdUsingColNumbers(int traineeId) throws SQLException {
        String selectQuery = "select * from trainee where id = ?";
        try (PreparedStatement stmt = JdbcUtils.getConnection().prepareStatement(selectQuery)) {
            stmt.setInt(1, traineeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String firstName = rs.getString(2);
                    String lastName = rs.getString(3);
                    int rating = rs.getInt(4);
                    return new Trainee(traineeId, firstName, lastName, rating);
                }
            }
            return null;
        }
    }

    //5. Получает все Trainee из базы данных, используя метод получения “по именам полей”.
    // Если ни одного Trainee в БД нет, возвращает пустой список.
    public static List<Trainee> getTraineesUsingColNames() throws SQLException {
        String selectQuery = "select * from trainee";
        List<Trainee> trainees = new ArrayList<>();
        try (PreparedStatement stmt = JdbcUtils.getConnection().prepareStatement(selectQuery); ResultSet rs = stmt.executeQuery(selectQuery)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                int rating = rs.getInt("rating");
                trainees.add(new Trainee(id, firstName, lastName, rating));
            }
        }
        return trainees;
    }

    //6. Получает все Trainee из базы данных, используя метод получения “по номерам полей”.
    // Если ни одного Trainee в БД нет, возвращает пустой список.
    public static List<Trainee> getTraineesUsingColNumbers() throws SQLException {
        String selectQuery = "select * from trainee";
        List<Trainee> trainees = new ArrayList<>();
        try (PreparedStatement stmt = JdbcUtils.getConnection().prepareStatement(selectQuery); ResultSet rs = stmt.executeQuery(selectQuery)) {
            while (rs.next()) {
                int id = rs.getInt(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                int rating = rs.getInt(4);
                trainees.add(new Trainee(id, firstName, lastName, rating));
            }
        }
        return trainees;
    }

    //7. Удаляет Trainee из базы данных.
    public static void deleteTrainee(Trainee trainee) throws SQLException {
        String deleteQuery = "delete from trainee where id = ?";
        try (PreparedStatement stmt = JdbcUtils.getConnection().prepareStatement(deleteQuery)) {
            stmt.setInt(1, trainee.getId());
            stmt.executeUpdate();
        }
    }

    //8. Удаляет все Trainee из базы данных
    public static void deleteTrainees() throws SQLException {
        String deleteQuery = "delete from trainee";
        try (PreparedStatement stmt = JdbcUtils.getConnection().prepareStatement(deleteQuery)) {
            stmt.executeUpdate();
        }
    }

    //9. Добавляет Subject в базу данных
    public static void insertSubject(Subject subject) throws SQLException {
        String insertQuery = "insert into subject values(?,?)";
        int result;
        try (PreparedStatement stmt = JdbcUtils.getConnection().prepareStatement(insertQuery, com.mysql.jdbc.Statement.RETURN_GENERATED_KEYS)) {
            stmt.setNull(1, java.sql.Types.INTEGER);
            stmt.setString(2, subject.getName());
            result = stmt.executeUpdate();
            if (result > 0) {
                try(ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        subject.setId(rs.getInt(1));
                        rs.close();
                    }
                }
            }
        }
    }

    //10. Получает Subject  из базы данных по его ID, используя метод получения “по именам полей”.
    // Если Subject с таким ID нет, возвращает null.
    public static Subject getSubjectByIdUsingColNames(int subjectId) throws SQLException {
        String selectQuery = "select * from subject where id = ?";
        try (PreparedStatement stmt = JdbcUtils.getConnection().prepareStatement(selectQuery)) {
            stmt.setInt(1, subjectId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    return new Subject(subjectId, name);
                }
            }
            return null;
        }
    }

    //11. Получает Subject  из базы данных по его ID, используя метод получения “по номерам полей”.
    // Если Subject с таким ID нет, возвращает null.
    public static Subject getSubjectByIdUsingColNumbers(int subjectId) throws SQLException {
        String selectQuery = "select * from subject where id = ?";
        try (PreparedStatement stmt = JdbcUtils.getConnection().prepareStatement(selectQuery)) {
            stmt.setInt(1, subjectId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString(2);
                    return new Subject(subjectId, name);
                }
            }
            return null;
        }
    }

    //12. Удаляет все Subject из базы данных.
    public static void deleteSubjects() throws SQLException {
        String deleteQuery = "delete from subject";
        try (PreparedStatement stmt = JdbcUtils.getConnection().prepareStatement(deleteQuery)) {
            stmt.executeUpdate();
        }
    }

    //13. Добавляет School в базу данных
    public static void insertSchool(School school) throws SQLException {
        String insertQuery = "insert into school values(?,?,?)";
        int result;
        try (PreparedStatement stmt = JdbcUtils.getConnection().prepareStatement(insertQuery, com.mysql.jdbc.Statement.RETURN_GENERATED_KEYS)) {
            stmt.setNull(1, java.sql.Types.INTEGER);
            stmt.setString(2, school.getName());
            stmt.setInt(3, school.getYear());
            result = stmt.executeUpdate();
            if (result > 0) {
                try(ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        school.setId(rs.getInt(1));
                        rs.close();
                    }
                }
            }
        }
    }

    //14. Получает School  из базы данных по ее ID, используя метод получения “по именам полей”.
    // Если School с таким ID нет, возвращает null.
    public static School getSchoolByIdUsingColNames(int schoolId) throws SQLException {
        String selectQuery = "select * from school where id = ?";
        try (PreparedStatement stmt = JdbcUtils.getConnection().prepareStatement(selectQuery)) {
            stmt.setInt(1, schoolId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    int year = rs.getInt("year");
                    return new School(schoolId, name, year);
                }
            }
            return null;
        }
    }

    //15. Получает School  из базы данных по ее ID, используя метод получения “по номерам полей”.
    // Если School с таким ID нет, возвращает null.
    public static School getSchoolByIdUsingColNumbers(int schoolId) throws SQLException {
        String selectQuery = "select * from school where id = ?";
        try (PreparedStatement stmt = JdbcUtils.getConnection().prepareStatement(selectQuery)) {
            stmt.setInt(1, schoolId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString(2);
                    int year = rs.getInt(3);
                    return new School(schoolId, name, year);
                }
            }
            return null;
        }
    }

    //16. Удаляет все School из базы данных. Если список Group в School не пуст, удаляет все Group для каждой School.
    public static void deleteSchools() throws SQLException {
        String deleteQuery = "delete from school";
        try (PreparedStatement stmt = JdbcUtils.getConnection().prepareStatement(deleteQuery)) {
            stmt.executeUpdate();
        }
    }

    //17. Добавляет Group в базу данных, устанавливая ее принадлежность к школе School.
    public static void insertGroup(School school, Group group) throws SQLException {
        String insertQuery = "insert into `group` values(?,?,?,?)";
        int result;
        try (PreparedStatement stmt = JdbcUtils.getConnection().prepareStatement(insertQuery, com.mysql.jdbc.Statement.RETURN_GENERATED_KEYS)) {
            stmt.setNull(1, java.sql.Types.INTEGER);
            stmt.setInt(2, school.getId());
            stmt.setString(3, group.getName());
            stmt.setString(4, group.getRoom());
            result = stmt.executeUpdate();
            if (result > 0) {
                try(ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        group.setId(rs.getInt(1));
                        rs.close();
                    }
                }
            }
        }
    }

    //18. Получает School по ее ID вместе со всеми ее Group из базы данных. Если School с таким ID нет,
    // возвращает null. Метод получения (по именам или номерам полей) - на Ваше усмотрение.
    public static School getSchoolByIdWithGroups(int id) throws SQLException {
        String selectQuery = "select * from school, `group` where school.id = ?";
        School school = null;
        List<Group> groups = new ArrayList<>();

        boolean schoolWasAdded = false;
        try (PreparedStatement stmt = JdbcUtils.getConnection().prepareStatement(selectQuery)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    if (!schoolWasAdded) {
                        String name = rs.getString(2);
                        int year = rs.getInt(3);
                        school = new School(id, name, year);
                        schoolWasAdded = true;
                    }
                    int groupId = rs.getInt(4);
                    String groupName = rs.getString(6);
                    String groupRoom = rs.getString(7);
                    groups.add(new Group(groupId, groupName, groupRoom));
                }
            }
            if (schoolWasAdded) {
                school.setGroups(groups);
            }
            return school;
        }
    }

    //19. Получает список всех School вместе со всеми их Group из базы данных.
    // Если ни одной School в БД нет, возвращает пустой список.
    // Метод получения (по именам или номерам полей) - на Ваше усмотрение.
    public static List<School> getSchoolsWithGroups() throws SQLException {
        String selectQuery = "select * from school inner join `group` on school.id = `group`.schoolid";
        Map<Group, School> map = new HashMap<>();
        List<School> schools = new ArrayList<>();
        School school = new School();

        try (PreparedStatement stmt = JdbcUtils.getConnection().prepareStatement(selectQuery); ResultSet rs = stmt.executeQuery(selectQuery)) {
            while (rs.next()) {
                int schoolId = rs.getInt(1);
                String schoolName = rs.getString(2);
                int schoolYear = rs.getInt(3);
                if (school.getId() != schoolId) {
                    school = new School(schoolId, schoolName, schoolYear);
                }
                int groupId = rs.getInt(4);
                String groupName = rs.getString(6);
                String groupRoom = rs.getString(7);
                map.put(new Group(groupId, groupName, groupRoom), school);
                if (!schools.contains(school)) {
                    schools.add(school);
                }
            }
            for (Group group: map.keySet()) {
                map.get(group).addGroup(group);
            }
            return schools;
        }
    }
}
