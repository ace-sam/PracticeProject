package com.practice.redo.practice.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDAO{

    private final JdbcTemplate jdbcTemplate;
    private final  CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {

        var sql= """
                select id, name, email, age
                FROM customer
                """;

//        RowMapper<Customer> rowMapper= (rs, rowNum)->{
//            Customer customer =new Customer(
//                    rs.getInt("id"),
//                    rs.getString("name"),
//                    rs.getString("email"),
//                    rs.getInt("age")
//            );
//           return customers;
//        };
//      List<Customer> customers= jdbcTemplate.query(sql, rowMapper);

        // Or using your own RowMapper

        List<Customer> customers= jdbcTemplate.query(sql, customerRowMapper);

        return customers;

    }

    @Override
    public Optional<Customer> selectCustomerById(long id) {

        var sql= """
                SELECT id, name, email, age
                FROM customer
                WHERE id=?
                """;

        return jdbcTemplate.query(sql, customerRowMapper, id).stream().findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql= """
                INSERT INTO customer(name, email, age)
                VALUES (?,?,?)
                """;

        int update = jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge()
                );

        System.out.println(update); // returns nbt of rows affected
    }

    @Override
    public boolean existsPersonWithEmail(String email) {

        var sql= """
                select count(id)
                from customer
                where email =?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existsCustomerByID(long id) {
        var sql= """
                select count(id)
                from customer
                where id =?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void deleteCustomer(long id) {
        var sql = """
                delete
                from customer
                where id = ?
                """;

        int c=jdbcTemplate.update(sql, id);
        System.out.println(c);
    }

    @Override
    public void updateCustomer(Customer update) {

        if (update.getName() != null){
            String sql = "UPDATE customer SET name= ? WHERE id = ?";
            jdbcTemplate.update(sql, update.getName(), update.getId());
        }

        if (update.getEmail() != null){
            String sql = "UPDATE customer SET email= ? WHERE id = ?";
            jdbcTemplate.update(sql, update.getEmail(), update.getId());
        }

        if (update.getAge() != 0){
            String sql = "UPDATE customer SET age= ? WHERE id = ?";
            jdbcTemplate.update(sql, update.getAge(), update.getId());
        }


    }
}
