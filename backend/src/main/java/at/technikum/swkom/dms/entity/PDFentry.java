package at.technikum.swkom.dms.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PDFs")


public class PDFentry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fileName")
    private String fileName;

    @Column(name = "uploadDate")
    private String uploadDate;

    @Column(name = "fileSize")
    private String fileSize;

    @Column(name = "fileContent")
    private String fileContent;

    @Column(name = "fileURL")
    private String fileURL;
}
