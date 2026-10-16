package com.todolist.todoserver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tasklist")
public class Tasklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_list")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @OneToMany(mappedBy = "taskList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        this.tasks.add(task);
        task.setTasklist(this);
    }

    public void removeTask(Task task) {
        this.tasks.remove(task);
        task.setTasklist(null);
    }
}
