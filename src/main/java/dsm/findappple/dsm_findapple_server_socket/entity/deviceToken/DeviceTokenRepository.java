package dsm.findappple.dsm_findapple_server_socket.entity.deviceToken;

import dsm.findappple.dsm_findapple_server_socket.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
    List<DeviceToken> findAllByUser(User user);

    @Query("select d.deviceToken from DeviceToken d where d.user = ?1")
    List<String> getDeviceTokensByUser(User user);
}
