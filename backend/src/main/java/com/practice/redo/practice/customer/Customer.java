package com.practice.redo.practice.customer;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
// Added in JDBC phase
@Table(
        name = "customer",
        uniqueConstraints = {@UniqueConstraint(
                name = "customer_email_unique",
                columnNames = "email"
        )}
)
public class Customer {

    @Id
    @SequenceGenerator(
            name= "customer_id_seq",
            sequenceName = "customer_id_seq", /* renamed from customer_id_sequence (to match the current table I guess)*/
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_id_seq"  // renamed from customer_id_sequence
    )
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    private int age;

    public Customer(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Customer(long id, String name, String email, int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Customer() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String lastname) {
        this.email = lastname;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id && age == customer.age && Objects.equals(name, customer.name) && Objects.equals(email, customer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, age);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
