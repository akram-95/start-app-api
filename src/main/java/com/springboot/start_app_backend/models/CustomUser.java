package com.springboot.start_app_backend.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public interface CustomUser {

    public String getUsername();

    public long getId();

    public String getEmail();

    public long getCreation_date();

    public Set<String> getroles();


}
