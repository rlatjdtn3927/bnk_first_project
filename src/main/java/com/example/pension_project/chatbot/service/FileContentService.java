package com.example.pension_project.chatbot.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileContentService {
	//지원하는 확장자 리스트(이 확장자만 파싱대상)
    private final List<String> SUPPORTED_EXT = List.of("pdf", "doc", "docx", "xls", "xlsx", "zip");

    public List<String> listAllParsedFiles() {
        List<String> result = new ArrayList<>();
        result.addAll(listFilesInDirectory("static/pdf/manual"));
        result.addAll(listFilesInDirectory("static/pdf/asset"));
        return result;
    }

    private List<String> listFilesInDirectory(String path) {
        List<String> filesList = new ArrayList<>();
        try {
            File dir = new ClassPathResource(path).getFile();
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.isFile()) continue;
                    String ext = getExtension(file.getName());
                    if (SUPPORTED_EXT.contains(ext)) {
                        filesList.add(file.getName());
                    }
                }
            }
        } catch (Exception e) {
            filesList.add("[디렉토리 파싱 실패: " + path + " | " + e.getMessage() + "]");
        }
        return filesList;
    }
    
    //전체 파싱결과 로딩
    public String loadAllText() {
        StringBuilder sb = new StringBuilder();

        List<String> manualFiles = listFilesInDirectory("static/pdf/manual");
        List<String> assetFiles = listFilesInDirectory("static/pdf/asset");
        int totalCount = manualFiles.size() + assetFiles.size();

        // ✅ 파일 수 정보를 먼저 context에 추가
        sb.append("[총 파싱된 파일 수: ").append(totalCount).append("개]\n\n");

        // ✅ 실제 텍스트 파싱
        sb.append(parseDirectory("static/pdf/manual")).append("\n");
        sb.append(parseDirectory("static/pdf/asset")).append("\n");
        sb.append(parseTextFile("static/html_content.txt")).append("\n");

        return sb.toString();
    }

    //디렉토리 내 파일 이름 목록 반환
    public List<String> getFileNames() {
        List<String> allFiles = new ArrayList<>();
        allFiles.addAll(listFilesInDirectory("static/pdf/manual"));
        allFiles.addAll(listFilesInDirectory("static/pdf/asset"));
        return allFiles;
    }
    
    //디렉토리 내 파일 텍스트 파싱
    private String parseDirectory(String path) {
        StringBuilder text = new StringBuilder();
        try {
            File dir = new ClassPathResource(path).getFile();
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.isFile()) continue;
                    String ext = getExtension(file.getName());
                    if (SUPPORTED_EXT.contains(ext)) {
                        text.append("==== [").append(file.getName()).append("] ====\n");
                        text.append(parseFile(file, ext)).append("\n");
                    }
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            text.append("[디렉토리 파싱 실패: ").append(path).append(" | ").append(e.getMessage()).append("]\n");
        }
        return text.toString();
    }

    //HTMl 하드코딩 텍스트 파싱 및 정제
    private String parseTextFile(String path) {
        try (InputStream is = new ClassPathResource(path).getInputStream()) {
            String raw = new BufferedReader(new InputStreamReader(is))
                    .lines()
                    .collect(Collectors.joining("\n"));
            return cleanHtmlText(raw);
        } catch (Exception e) {
            return "[html_content.txt 파싱 실패 or 파일 없음]";
        }
    }

    //단일 파일 파싱
    private String parseFile(File file, String ext) {
        try {
            switch (ext) {
                case "pdf":   return parsePdf(file);
                case "doc":   return parseDoc(file);
                case "docx":  return parseDocx(file);
                case "xls":
                case "xlsx":  return parseExcel(file);
                case "zip":   return parseZip(file);
                default:      return "[지원하지 않는 확장자: " + ext + "]";
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return "[파일 파싱 실패: " + file.getName() + " | " + e.getMessage() + "]";
        }
    }

    //pdf 파일 처리
    private String parsePdf(File file) {
        try (PDDocument doc = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(doc);
        } catch (Exception e) {
            //e.printStackTrace();
            return "[PDF 파싱 실패: " + file.getName() + " | " + e.getMessage() + "]";
        }
    }

    //doc 파일 처리
    private String parseDoc(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            try {
                HWPFDocument doc = new HWPFDocument(fis);
                WordExtractor extractor = new WordExtractor(doc);
                return extractor.getText();
            } catch (org.apache.poi.poifs.filesystem.OfficeXmlFileException e) {
                // 확장자는 doc인데 실제로는 docx일 때
                return parseDocx(file);
            } catch (Exception e) {
                //e.printStackTrace();
                return "[DOC 파싱 실패: " + file.getName() + " | " + e.getMessage() + "]";
            }
        } catch (Exception e) {
           // e.printStackTrace();
            return "[파일 열기 실패: " + file.getName() + " | " + e.getMessage() + "]";
        }
    }

    //docx 파일 처리
    private String parseDocx(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            XWPFDocument doc = new XWPFDocument(fis);
            return doc.getParagraphs().stream()
                .map(XWPFParagraph::getText)
                .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            ///e.printStackTrace();
            return "[DOCX 파싱 실패: " + file.getName() + " | " + e.getMessage() + "]";
        }
    }

    //엑셀 파일 처리
    private String parseExcel(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            Workbook workbook = WorkbookFactory.create(fis);
            StringBuilder sb = new StringBuilder();
            for (Sheet sheet : workbook) {
                for (Row row : sheet) {
                    for (Cell cell : row) {
                        sb.append(cell.toString()).append(" | ");
                    }
                    sb.append("\n");
                }
            }
            return sb.toString();
        } catch (Exception e) {
            //e.printStackTrace();
            return "[EXCEL 파싱 실패: " + file.getName() + " | " + e.getMessage() + "]";
        }
    }
    //알집 파일 처리
    private String parseZip(File file) {
        StringBuilder result = new StringBuilder();
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(file))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String entryName = entry.getName();
                result.append("---- ZIP 내부 파일: ").append(entryName).append(" ----\n");
                if (!entry.isDirectory()) {
                    // 임시파일 생성해서 재귀 파싱
                    File temp = File.createTempFile("unzip-", "-" + entryName.replace("/", "_"));
                    try (FileOutputStream fos = new FileOutputStream(temp)) {
                        byte[] buffer = new byte[2048];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                    String innerExt = getExtension(entryName);
                    if (SUPPORTED_EXT.contains(innerExt)) {
                        result.append(parseFile(temp, innerExt));
                    } else {
                        result.append("[지원하지 않는 확장자: ").append(innerExt).append("]\n");
                    }
                    temp.delete();
                }
                zis.closeEntry();
            }
        } catch (Exception e) {
            //e.printStackTrace();
            result.append("[ZIP 파싱 실패: ").append(file.getName()).append(" | ").append(e.getMessage()).append("]\n");
        }
        return result.toString();
    }

    //파일 확장자 구하기
    private String getExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        return dot > 0 ? filename.substring(dot + 1).toLowerCase() : "";
    }
    
    private String cleanHtmlText(String rawHtml) {
        try {
            Document doc = Jsoup.parse(rawHtml);
            doc.outputSettings(new Document.OutputSettings().prettyPrint(true));
            String plainText = doc.text();

            // 옵션: 너무 긴 연속 공백 제거, 줄바꿈 추가
            return plainText.replaceAll("[ ]{2,}", " ")
                            .replaceAll("(?<=[가-힣])\\.(?=[가-힣])", ".\n");  // 문장 구분 줄바꿈
        } catch (Exception e) {
            return "[html 정제 실패: " + e.getMessage() + "]";
        }
    }
}
