package nom.aob.sb.sbbench02.model;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SimpleResponse {
    private String hostString;
    private String pathString;
    private String timeString;
    private Integer randomInteger;
    private Long threadID;
}
