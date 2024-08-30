package spotlight.spotlight_ver2;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.FirebaseApp;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@OpenAPIDefinition(servers = {@Server(url = "http://localhost:8080", description = "Server URL")})

@SpringBootApplication
public class SpotlightVer2Application {

    public static void main(String[] args) {
        SpringApplication.run(SpotlightVer2Application.class, args);
        initializeFirebase();
    }

    public static void initializeFirebase() {
        try (InputStream serviceAccount = SpotlightVer2Application.class.getClassLoader().getResourceAsStream("serviceAccountKey.json")) {
            if (serviceAccount == null) {
                throw new FileNotFoundException("serviceAccountKey.json 파일을 찾을 수 없음.");
            }
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket("spotlight-c7eb0.appspot.com")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            System.err.println("Firebase 초기화 에러");
            e.printStackTrace();
        }
    }

}
