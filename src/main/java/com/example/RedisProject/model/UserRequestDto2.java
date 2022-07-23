package com.example.RedisProject.model;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
@Data
public class UserRequestDto2  implements Serializable {

    private Long id;
    private String name;

    private int age = 0;
    String dob;
    private String location;

    public UserRequestDto2(Long id, String name, int age, @NotEmpty String dob, String location) {
        this.id=id;
        this.name = name;
        this.age=age;
        this.dob=dob;
        this.location = location;

    }

}
