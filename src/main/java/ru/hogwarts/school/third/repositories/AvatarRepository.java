package ru.hogwarts.school.third.repositories;
import ru.hogwarts.school.third.model.Avatar;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    Optional<Avatar> findByStudentId(Long studentId);
}
