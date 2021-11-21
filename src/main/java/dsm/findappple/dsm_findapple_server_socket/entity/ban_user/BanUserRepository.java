package dsm.findappple.dsm_findapple_server_socket.entity.ban_user;

import dsm.findappple.dsm_findapple_server_socket.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BanUserRepository extends JpaRepository<BanUser, Long> {
    boolean existsByUserAndBanUser(User user, User banUser);
}
