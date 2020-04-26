package com.kartoffelkopf.quiz.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.kartoffelkopf.quiz.data.QuestionDao;
import com.kartoffelkopf.quiz.data.QuizDao;
import com.kartoffelkopf.quiz.data.RoundDao;
import com.kartoffelkopf.quiz.model.Question;
import com.kartoffelkopf.quiz.model.Quiz;
import com.kartoffelkopf.quiz.model.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

@Service
public class RoundServiceImpl implements RoundService {

    @Autowired
    private RoundDao roundDao;

    @Autowired
    private QuizDao quizDao;

    @Override
    public Iterable<Round> findAll() {
        return roundDao.findAll();
    }

    @Override
    public Optional<Round> findById(long id) {
        return roundDao.findById(id);
    }

    @Override
    public Round save(Round round) {
        return roundDao.save(round);
    }

    @Override
    public void delete(Round round) {
        roundDao.delete(round);
    }

    class MyCellField implements PdfPCellEvent {
        protected String fieldname;
        public MyCellField(String fieldname) {
            this.fieldname = fieldname;
        }
        public void cellLayout(PdfPCell cell, Rectangle rectangle, PdfContentByte[] canvases) {
            final PdfWriter writer = canvases[0].getPdfWriter();
            final TextField textField = new TextField(writer, rectangle, fieldname);
            try {
                final PdfFormField field = textField.getTextField();
                writer.addAnnotation(field);
            } catch (final IOException | DocumentException ioe) {
                throw new ExceptionConverter(ioe);
            }
        }
    }

    @Override
    public Round addQuestion(long roundId, Question question) {
        Round round = roundDao.findById(roundId).get();
        round.getQuestions().add(question);
        return roundDao.save(round);
    }

    @Override
    public Quiz getQuiz(Round round) {
        for (Quiz q : quizDao.findAll()) {
            for (Round r : q.getRounds()) {
                if (r.getId() == round.getId()) {
                    return q;
                }
            }
        }
        return null;
    }

    @Override
    public Round removeQuestion(Round round, Question question) {
        List<Question> questions = round.getQuestions();
        questions.remove(question);
        round.setQuestions(questions);
        roundDao.save(round);
        return round;
    }

    @Override
    public int getNextQuestionNumber(Round round) {
        int questionNumber = 0;
        for (Question q : round.getQuestions()) {
            if (q.getQuestionNumber() > questionNumber) {
                questionNumber = q.getQuestionNumber();
            }
        }
        return questionNumber + 1;
    }

    @Override
    public ResponseEntity<byte[]> generatePdf(Round round) throws IOException, DocumentException {
        File file = new File("./" + getQuiz(round).getName() + "/" + round.getName() + ".pdf");
        file.getParentFile().mkdirs();
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        Paragraph header = new Paragraph(round.getName(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24));
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);

        document.add(new Paragraph(" "));

        if (round.getRoundType().getId() == 2L) {
            PdfPTable table = new PdfPTable(3);
            table.setTotalWidth(new float[]{30, 200, 300,});
            table.setLockedWidth(true);

            for (Question q : round.getQuestions()) {
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
                cell.setCellEvent(new MyCellField("answer_" + q.getQuestionNumber()));
                table.addCell(cell);
            }
            document.add(table);

        } else {
            PdfPTable table = new PdfPTable(2);
            table.setTotalWidth(new float[]{30, 300});
            table.setLockedWidth(true);

            for (Question q : round.getQuestions()) {
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
                cell.setCellEvent(new MyCellField("answer_" + q.getQuestionNumber()));
                table.addCell(cell);

            }
            document.add(table);
        }
        document.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename(round.getName() + ".pdf").build());
        return new ResponseEntity<>(Files.readAllBytes(file.toPath()), headers, HttpStatus.OK);
    }

}
