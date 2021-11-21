package dsm.findappple.dsm_findapple_server_socket.entity.user;

import dsm.findappple.dsm_findapple_server_socket.entity.area.Area;
import dsm.findappple.dsm_findapple_server_socket.entity.find.Find;
import dsm.findappple.dsm_findapple_server_socket.entity.lost.Lost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private Long kakaoId;

    private String kakaoNickName;

    private Integer point;

    private String profileUrl;

    @OneToMany(mappedBy = "user")
    private List<Lost> losts;

    @OneToMany(mappedBy = "user")
    private List<Find> finds;

    @OneToOne
    @JoinColumn(name = "visit_area")
    private Area area;

    public User updateUserName(String kakaoNickName) {
        this.kakaoNickName = kakaoNickName;

        return this;
    }

    public User updateProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;

        return this;
    }

    public User upPoint(Integer point) {
        this.point += point;

        return this;
    }

    public User downPoint(Integer point) {
        this.point -= point;

        return this;
    }
}
