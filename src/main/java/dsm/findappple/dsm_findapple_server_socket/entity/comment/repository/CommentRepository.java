package dsm.findappple.dsm_findapple_server_socket.entity.comment.repository;

import dsm.findappple.dsm_findapple_server_socket.entity.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
