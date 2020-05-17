package com.kartoffelkopf.quiz.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.kartoffelkopf.quiz.data.QuizDao;
import com.kartoffelkopf.quiz.model.Question;
import com.kartoffelkopf.quiz.model.Quiz;
import com.kartoffelkopf.quiz.model.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizDao quizDao;

    @Autowired
    private RoundService roundService;

    @Override
    public Iterable<Quiz> findAll() {
        return quizDao.findAll();
    }

    @Override
    public Optional<Quiz> findById(long id) {
        return quizDao.findById(id);
    }

    @Override
    public Quiz save(Quiz quiz) {
        return quizDao.save(quiz);
    }

    @Override
    public void delete(Quiz quiz) {
        quizDao.delete(quiz);
    }

    @Override
    public Quiz addRound(long quizId, Round round) {
        Quiz quiz = quizDao.findById(quizId).get();
        quiz.getRounds().add(round);
        return quizDao.save(quiz);
    }

    @Override
    public Quiz removeRound(Quiz quiz, Round round) {
        List<Round> rounds = quiz.getRounds();
        rounds.remove(round);
        quiz.setRounds(rounds);
        quizDao.save(quiz);
        return quiz;
    }

    @Override
    public ResponseEntity<byte[]> generatePdf(Quiz quiz) throws IOException, DocumentException {
        File file = new File("./" + quiz.getName() + "/" + quiz.getName() + ".pdf");
        file.getParentFile().mkdirs();
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        for (Round r : quiz.getRounds()) {

            Paragraph header = new Paragraph(r.getName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24));
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            document.add(new Paragraph(" "));

            PdfPTable table;
            if (r.getRoundType().getId() == 2L) {
                table = new PdfPTable(3);
                table.setTotalWidth(new float[]{30, 200, 300,});
                table.setLockedWidth(true);

                for (Question q : r.getQuestions()) {
                    Phrase phrase = new Phrase(Integer.toString(q.getQuestionNumber()));
                    phrase.setFont(FontFactory.getFont(FontFactory.HELVETICA, 14));
                    PdfPCell cell = new PdfPCell(phrase);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    Image image = Image.getInstance(q.getPicture().getBytes());
                    table.addCell(image);

                    cell = new PdfPCell();
                    cell.setFixedHeight(40);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setCellEvent(new RoundServiceImpl.MyCellField("answer_" + r.getName() + "_" + q.getQuestionNumber()));
                    table.addCell(cell);
                }

            } else {
                table = new PdfPTable(2);
                table.setTotalWidth(new float[]{30, 300});
                table.setLockedWidth(true);

                for (Question q : r.getQuestions()) {
                    Phrase phrase = new Phrase(Integer.toString(q.getQuestionNumber()));
                    phrase.setFont(FontFactory.getFont(FontFactory.HELVETICA, 14));
                    PdfPCell cell = new PdfPCell(phrase);
                    cell.setFixedHeight(40);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    cell = new PdfPCell();
                    cell.setFixedHeight(40);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setCellEvent(new RoundServiceImpl.MyCellField("answer_" + r.getName() + "_"  + q.getQuestionNumber()));
                    table.addCell(cell);

                }
            }
            document.add(table);
            document.newPage();
        }
        document.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename(quiz.getName() + ".pdf").build());
        return new ResponseEntity<>(Files.readAllBytes(file.toPath()), headers, HttpStatus.OK);
    }


}
