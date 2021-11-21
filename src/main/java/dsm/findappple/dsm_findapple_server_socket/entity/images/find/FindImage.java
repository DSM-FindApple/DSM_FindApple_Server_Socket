package dsm.findappple.dsm_findapple_server_socket.entity.images.find;

import dsm.findappple.dsm_findapple_server_socket.entity.find.Find;
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
public class FindImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long findImageId;

    @ManyToOne
    @JoinColumn(name = "find_id")
    private Find find;

    private String imageName;
}
