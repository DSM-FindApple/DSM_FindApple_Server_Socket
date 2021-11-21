package dsm.findappple.dsm_findapple_server_socket.entity.find;

import dsm.findappple.dsm_findapple_server_socket.entity.user.User;
import dsm.findappple.dsm_findapple_server_socket.payload.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FindRepository extends JpaRepository<Find, Long> {
    Optional<Find> findByFindId(Long findId);
    Page<Find> findAllByUser(User user, Pageable pageable);
    Page<Find> findAllByCategory(Category category, Pageable pageable);
    Page<Find> findAllByTitleContaining(String title, Pageable pageable);
    void deleteByFindId(Long findId);
    int countAllByUser(User user);
    Page<Find> findAllByArea_LongitudeGreaterThanEqualAndArea_LongitudeLessThanEqualAndArea_LatitudeGreaterThanEqualAndArea_LatitudeLessThanEqualOrderByWriteAtDesc(Double area_longitude, Double area_longitude2, Double area_latitude, Double area_latitude2, Pageable pageable);
    List<Find> findAllByArea_LongitudeGreaterThanEqualAndArea_LongitudeLessThanEqualAndArea_LatitudeGreaterThanEqualAndArea_LatitudeLessThanEqualOrderByWriteAtDesc(Double area_longitude, Double area_longitude2, Double area_latitude, Double area_latitude2);
    Page<Find> findAll(Pageable pageable);
}
