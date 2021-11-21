package dsm.findappple.dsm_findapple_server_socket.entity.comment;

import dsm.findappple.dsm_findapple_server_socket.entity.find.Find;
import dsm.findappple.dsm_findapple_server_socket.entity.lost.Lost;
import dsm.findappple.dsm_findapple_server_socket.entity.recomment.Recomment;
import dsm.findappple.dsm_findapple_server_socket.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "kakao_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "lost_id")
    private Lost lost;

    @ManyToOne
    @JoinColumn(name = "find_id")
    private Find find;

    private String comment;

    private LocalDateTime writeAt;

    @OneToMany(mappedBy = "comment")
    private List<Recomment> recomments;
}
