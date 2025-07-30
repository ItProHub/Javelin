package site.itprohub.javelin.data.entity;

import java.math.BigDecimal;

import site.itprohub.javelin.annotations.data.DbColumn;
import site.itprohub.javelin.annotations.data.DbEntity;

@DbEntity(tableName = "employees")
public class Employee {
    @DbColumn(isPrimaryKey = true)
    private int id;

    @DbColumn
    private String name;

    @DbColumn(field = "position", isPrimaryKey = false)
    private String position;

    @DbColumn
    private BigDecimal salary;

    public Employee() {
    }

    public Employee(String name, String position, BigDecimal salary) {
        this.name = name;
        this.position = position;
        this.salary = salary;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
