package dsm.findappple.dsm_findapple_server_socket.entity.deviceToken;

import dsm.findappple.dsm_findapple_server_socket.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
    boolean existsByUserAndDeviceToken(User user, String deviceToken);
    Optional<DeviceToken> findByDeviceTokenAndUser(String deviceToken, User user);
    void deleteByDeviceTokenAndUser(String deviceToken, User user);
}
