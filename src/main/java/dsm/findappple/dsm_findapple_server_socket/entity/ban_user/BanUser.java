package dsm.findappple.dsm_findapple_server_socket.entity.ban_user;

import dsm.findappple.dsm_findapple_server_socket.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BanUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long banId;

    @ManyToOne
    @JoinColumn(name = "kakao_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ban_user_id")
    private User banUser;
}
