package com.traveler.board.service;

//@Service("s3ImageStorageService")
//public class S3ImageStorageService implements ImageStorageService {
//
//    private final AmazonS3 s3;
//    private final String bucketName = "your-bucket";
//    private final String baseUrl = "https://your-bucket.kr.object.ncloudstorage.com/";
//
//    public S3ImageStorageService() {
//        BasicAWSCredentials credentials = new BasicAWSCredentials("YOUR_ACCESS_KEY", "YOUR_SECRET_KEY");
//        this.s3 = AmazonS3ClientBuilder.standard()
//                .withCredentials(new AWSStaticCredentialsProvider(credentials))
//                .withEndpointConfiguration(
//                        new AmazonS3ClientBuilder.EndpointConfiguration("https://kr.object.ncloudstorage.com", "kr-standard")
//                )
//                .build();
//    }
//
//    @Override
//    public String store(MultipartFile file) throws IOException {
//        String uuid = UUID.randomUUID().toString();
//        String ext = Objects.requireNonNull(file.getOriginalFilename())
//                .substring(file.getOriginalFilename().lastIndexOf('.'));
//        String fileName = uuid + ext;
//
//        // S3 업로드
//        s3.putObject(bucketName, fileName, file.getInputStream(), null);
//
//        // 접근 가능한 URL (권한 설정에 따라 다름)
//        return baseUrl + fileName;
//    }
//}
