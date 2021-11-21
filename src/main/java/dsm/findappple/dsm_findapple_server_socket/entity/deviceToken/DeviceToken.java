package dsm.findappple.dsm_findapple_server_socket.entity.deviceToken;

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
public class DeviceToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tokenId;

    private String deviceToken;

    @ManyToOne
    @JoinColumn(name = "kakaoId")
    private User user;
}
