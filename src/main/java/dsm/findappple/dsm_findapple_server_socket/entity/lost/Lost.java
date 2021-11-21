package dsm.findappple.dsm_findapple_server_socket.entity.lost;

import dsm.findappple.dsm_findapple_server_socket.entity.area.Area;
import dsm.findappple.dsm_findapple_server_socket.entity.comment.Comment;
import dsm.findappple.dsm_findapple_server_socket.entity.images.lost.LostImage;
import dsm.findappple.dsm_findapple_server_socket.entity.user.User;
import dsm.findappple.dsm_findapple_server_socket.payload.enums.Category;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Lost {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long lostId;

    private String title;

    @ManyToOne
    @JoinColumn(name = "kakaoId")
    private User user;

    @OneToOne
    @JoinColumn(name = "lost_area")
    private Area area;

    private String detailInfo;

    private LocalDateTime lostAt;

    private LocalDateTime writeAt;

    @Enumerated(value = EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "lost")
    private List<LostImage> lostImages;

    @OneToMany(mappedBy = "lost")
    private List<Comment> comments;
}
