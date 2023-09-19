package top.feiyangdigital.handleService;


import com.alibaba.fastjson2.JSONObject;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import top.feiyangdigital.entity.BaseInfo;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleCloudVisionService {

    public File downloadFileWithOkHttp(String url) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to download file: " + response);
            }

            File tempFile = Files.createTempFile("prefix-", ".suffix").toFile();
            try (FileOutputStream fos = new FileOutputStream(tempFile);
                 InputStream is = response.body().byteStream()) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }

            return tempFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ImageAnnotatorClient createClient() {
        try {
            // 使用BaseInfo读取的配置信息来创建Google Vision客户端
            JSONObject googleServiceAccountConfig = BaseInfo.getGoogleServiceAccountConfig();
            Credentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(googleServiceAccountConfig.toString().getBytes()));
            return ImageAnnotatorClient.create(ImageAnnotatorSettings.newBuilder().setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build());
        } catch (IOException e) {

            return null;
        }
    }

    public List<EntityAnnotation> detectTextFromRemoteImage(String imageUrl) {
        List<AnnotateImageRequest> requests = new ArrayList<>();
        ImageSource imgSource = ImageSource.newBuilder().setImageUri(imageUrl).build();
        Image img = Image.newBuilder().setSource(imgSource).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build();
        requests.add(request);

        try (ImageAnnotatorClient client = createClient()) {
            if (client == null) return new ArrayList<>();

            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            AnnotateImageResponse res = response.getResponses(0);

            if (res.hasError()) {
                return new ArrayList<>();
            }

            return res.getTextAnnotationsList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public SafeSearchAnnotation detectSafeSearchFromRemoteImage(String imageUrl) {
        List<AnnotateImageRequest> requests = new ArrayList<>();
        ImageSource imgSource = ImageSource.newBuilder().setImageUri(imageUrl).build();
        Image img = Image.newBuilder().setSource(imgSource).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.SAFE_SEARCH_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build();
        requests.add(request);

        try (ImageAnnotatorClient client = createClient()) {
            if (client == null) return null;

            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            AnnotateImageResponse res = response.getResponses(0);

            if (res.hasError()) {

                return null;
            }

            return res.getSafeSearchAnnotation();
        } catch (Exception e) {

            return null;
        }
    }

    public List<EntityAnnotation> detectTextFromLocalImage(File tempFile) {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        try {
            ByteString imgBytes = ByteString.readFrom(new FileInputStream(tempFile));
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feat)
                    .setImage(img)
                    .build();
            requests.add(request);

            try (ImageAnnotatorClient client = createClient()) {
                if (client == null) return new ArrayList<>();

                BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
                AnnotateImageResponse res = response.getResponses(0);

                if (res.hasError()) {
                    return new ArrayList<>();
                }

                return res.getTextAnnotationsList();
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public SafeSearchAnnotation detectSafeSearchFromLocalImage(File tempFile) {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        try {
            ByteString imgBytes = ByteString.readFrom(new FileInputStream(tempFile));
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Feature.Type.SAFE_SEARCH_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feat)
                    .setImage(img)
                    .build();
            requests.add(request);

            try (ImageAnnotatorClient client = createClient()) {
                if (client == null) return null;

                BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
                AnnotateImageResponse res = response.getResponses(0);

                if (res.hasError()) {
                    return null;
                }

                return res.getSafeSearchAnnotation();
            }
        } catch (Exception e) {
            return null;
        }
    }
}
