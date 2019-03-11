package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
		
	@Query("select count(m) from Message m where m.sender.id = ?1")
	Integer findNumberMessagesByActorId(int actorId);
	
	@Query("select count(m) from Message m where m.sender.id= ?1 and m.isSpam = true")
	Integer findNumberMessagesSpamByActorId(int actorId);
	
	@Query("select m from Message m where m.sender.id = ?1")
	Collection<Message> findMessagesByActorId(int actorId);
	
	
	
}
