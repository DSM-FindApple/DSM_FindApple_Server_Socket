package dsm.findappple.dsm_findapple_server_socket.entity.promise;

import dsm.findappple.dsm_findapple_server_socket.entity.area.Area;
import dsm.findappple.dsm_findapple_server_socket.entity.message.Message;
import dsm.findappple.dsm_findapple_server_socket.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Promise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long promiseId;

    @ManyToOne
    @JoinColumn(name = "kakao_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private User target;

    private String script;

    private LocalDateTime meetAt;

    private Boolean isAccept;

    @OneToOne
    @JoinColumn(name = "area_code")
    private Area area;

    @OneToOne
    @JoinColumn(name = "message_id")
    private Message message;
}
