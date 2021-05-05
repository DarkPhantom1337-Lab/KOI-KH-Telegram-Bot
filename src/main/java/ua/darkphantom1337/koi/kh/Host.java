package ua.darkphantom1337.koi.kh;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Host {
    public static void uploadToHost(String login, String pass, String path) {
        List<File> files = new ArrayList<File>();
        File file = new File(path);
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                if (f.isDirectory())
                    uploadFilesInDir(login, pass, f.getName(), Host.getFiles(f.getPath()));
                if (f.isFile())
                    files.add(f);
            }
            Host.uploadFiles(login, pass, files);
        } else {
            if (file.isFile()) {
                files.add(file);
                uploadFiles(login, pass, files);
            }
        }
    }

    public static void uploadFiles(String login, String pass, List<File> files) {
        for (File f : files) {
            try {
                HashMap<String, String> auth = keys(
                        content("https://api.files.fm/api/login.php?user=" + login + "&pass=" + pass).replace(" ", ""),
                        ";", "=");
                String hash = auth.get("root_upload_hash");
                HashMap<String, String> keys = keys(
                        content("https://api.files.fm/api/get_upload_keys.php?hash=" + hash + "&user=" + login
                                + "&pass=" + pass).replace("{", "").replace("}", "").replace("\"", "").replace(" ", ""),
                        ",", ":");
                String charset = "UTF-8";
                MultipartUtility multipart = new MultipartUtility(
                        "http://api.files.fm/save_file.php?up_id=" + hash + "&key=" + keys.get("AddFileKey"), charset);
                multipart.addFilePart("file", f);
                multipart.finish();
            } catch (Exception ignored) {
            }
        }
    }

    public static String uploadFilesInDir(String login, String pass, String dirname, List<File> files) {
        String hash = "NULL", key = "NULL";
        try {
            String data[] = content("https://api.files.fm/api/get_upload_id.php?user=" + login + "&pass=" + pass
                    + "&folder_name=" + dirname).split(",");
            hash = data[1].replace("\"hash\":\"", "").replace("\"", "");
            key = data[3].replace("\"add_key\": \"", "").replace("\"", "").replace(" ", "");
            for (File f : files) {
                if (f.isFile())
                    sendFile("http://api.files.fm/save_file.php?up_id=" + hash + "&key=" + key, f);
                if (f.isDirectory())
                    uploadFilesInDir(login, pass, dirname + "/" + f.getName(), getFiles(f.getPath()));
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return "HASH=" + hash + " KEY=" + key;
    }

    public static void sendFile(String url, File f) throws IOException {
        MultipartUtility multipart = new MultipartUtility(url, "UTF-8");
        multipart.addFilePart("file", f);
        multipart.finish();
    }

    public static HashMap<String, String> keys(String ss, String num, String pair) {
        HashMap<String, String> map = new HashMap<>();

        for (String s : ss.split(num)) {
            String[] split = s.split(pair);

            map.put(split[0], split[1]);
        }

        return map;
    }

    public static String content(String url) {

        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (Exception e) {
        }

        return "";
    }

    public static Integer getDirAmount(String path) {
        Integer i = 0;
        for (File f : new File(path).listFiles())
            if (f.isDirectory())
                i++;
        return i;
    }

    public static Integer getFileAmount(String path) {
        Integer i = 0;
        for (File f : new File(path).listFiles())
            if (f.isFile())
                i++;
        return i;
    }

    public static long getSpaceMB(String path) {
        long bytes = 0;
        for (File f : new File(path).listFiles())
            bytes += f.length();
        return bytes / (1024 * 1024);
    }

    public static List<File> getFiles(String path) {
        List<File> files = new ArrayList<File>();
        for (File f : new File(path).listFiles())
            files.add(f);
        return files;
    }

    public static class MultipartUtility {
        private final String boundary;
        private static final String LINE_FEED = "\r\n";
        private HttpURLConnection httpConn;
        private OutputStream outputStream;
        private PrintWriter writer;

        public MultipartUtility(String requestURL, String charset) throws IOException {

            // creates a unique boundary based on time stamp
            boundary = "===" + System.currentTimeMillis() + "===";

            URL url = new URL(requestURL);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setUseCaches(false);
            httpConn.setDoOutput(true); // indicates POST method
            httpConn.setDoInput(true);
            httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            httpConn.setRequestProperty("User-Agent", "CodeJava Agent");
            httpConn.setRequestProperty("Test", "Bonjour");
            outputStream = httpConn.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
        }

        public void addFilePart(String fieldName, File uploadFile) throws IOException {
            String fileName = uploadFile.getName();
            writer.append("--" + boundary).append(LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"")
                    .append(LINE_FEED);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
            writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
            writer.append(LINE_FEED);
            writer.flush();

            FileInputStream inputStream = new FileInputStream(uploadFile);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();

            writer.append(LINE_FEED);
            writer.flush();
        }

        public List<String> finish() throws IOException {
            List<String> response = new ArrayList<String>();

            writer.append(LINE_FEED).flush();
            writer.append("--" + boundary + "--").append(LINE_FEED);
            writer.close();

            // checks server's status code first
            int status = httpConn.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    response.add(line);
                }
                reader.close();
                httpConn.disconnect();
            } else {
                throw new IOException("Server returned non-OK status: " + status);
            }

            return response;
        }
    }
}
