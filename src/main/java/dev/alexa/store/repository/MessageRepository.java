package dev.alexa.store.repository;

import dev.alexa.store.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {


    Page<Message> findAllByDestination_Id(Long i, Pageable pageable);
}
