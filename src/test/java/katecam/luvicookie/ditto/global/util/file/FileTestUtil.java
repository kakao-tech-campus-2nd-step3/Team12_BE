package katecam.luvicookie.ditto.global.util.file;

import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

public class FileTestUtil {

    public static MockMultipartFile getTestImageFile() {
        try {
            return new MockMultipartFile("profile_image",
                    "test-img.jpg", "image/jpg",
                    new FileInputStream("src/test/resources/images/test-img.jpg")
            );
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
